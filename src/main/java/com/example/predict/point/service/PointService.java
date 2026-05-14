package com.example.predict.point.service;

import com.example.predict.match.domain.Match;
import com.example.predict.point.domain.PointTransaction;
import com.example.predict.point.domain.PointTransactionType;
import com.example.predict.point.domain.PointWallet;
import com.example.predict.point.dto.RankingResponse;
import com.example.predict.point.repository.PointTransactionRepository;
import com.example.predict.point.repository.PointWalletRepository;
import com.example.predict.prediction.domain.Prediction;
import com.example.predict.user.domain.User;
import com.example.predict.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class PointService {

    private static final long INITIAL_POINT = 1000;

    private final PointWalletRepository pointWalletRepository;
    private final PointTransactionRepository pointTransactionRepository;
    private final UserService userService;

    public PointService(PointWalletRepository pointWalletRepository,
                        PointTransactionRepository pointTransactionRepository,
                        UserService userService) {
        this.pointWalletRepository = pointWalletRepository;
        this.pointTransactionRepository = pointTransactionRepository;
        this.userService = userService;
    }

    @Transactional
    public PointWallet ensureWallet(User user) {
        return pointWalletRepository.findByUserId(user.getId())
                .orElseGet(() -> pointWalletRepository.save(new PointWallet(user, INITIAL_POINT)));
    }

    @Transactional
    public void bet(User user, Match match, Prediction prediction, long amount) {
        PointWallet wallet = pointWalletRepository.findByUserIdForUpdate(user.getId())
                .orElseGet(() -> pointWalletRepository.save(new PointWallet(user, INITIAL_POINT)));
        wallet.withdraw(amount);
        pointTransactionRepository.save(new PointTransaction(
                user,
                match,
                prediction,
                PointTransactionType.BET,
                -amount,
                wallet.getPoint()
        ));
    }

    @Transactional
    public void reward(User user, Match match, Prediction prediction, long amount) {
        PointWallet wallet = pointWalletRepository.findByUserIdForUpdate(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("포인트 지갑을 찾을 수 없습니다."));
        wallet.deposit(amount);
        pointTransactionRepository.save(new PointTransaction(
                user,
                match,
                prediction,
                PointTransactionType.REWARD,
                amount,
                wallet.getPoint()
        ));
    }

    @Transactional
    public long addByAdmin(Long userId, long amount) {
        User user = userService.getById(userId);
        ensureWallet(user);
        PointWallet wallet = pointWalletRepository.findByUserIdForUpdate(userId)
                .orElseThrow(() -> new EntityNotFoundException("포인트 지갑을 찾을 수 없습니다."));
        wallet.deposit(amount);
        pointTransactionRepository.save(new PointTransaction(
                user,
                null,
                null,
                PointTransactionType.ADMIN_ADJUST,
                amount,
                wallet.getPoint()
        ));
        return wallet.getPoint();
    }

    @Transactional(readOnly = true)
    public long getPoint(Long userId) {
        return pointWalletRepository.findByUserId(userId)
                .map(PointWallet::getPoint)
                .orElse(0L);
    }

    @Transactional(readOnly = true)
    public List<RankingResponse> rankings() {
        AtomicInteger rank = new AtomicInteger(1);
        return pointWalletRepository.findAll().stream()
                .sorted(Comparator.comparingLong(PointWallet::getPoint).reversed())
                .map(wallet -> {
                    User user = userService.getById(wallet.getUser().getId());
                    return new RankingResponse(
                            rank.getAndIncrement(),
                            user.getId(),
                            user.getName(),
                            user.getStudentId(),
                            wallet.getPoint()
                    );
                })
                .toList();
    }
}
