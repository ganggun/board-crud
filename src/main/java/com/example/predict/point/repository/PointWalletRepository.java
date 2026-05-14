package com.example.predict.point.repository;

import com.example.predict.point.domain.PointWallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import java.util.Optional;

public interface PointWalletRepository extends JpaRepository<PointWallet, Long> {

    Optional<PointWallet> findByUserId(Long userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select w from PointWallet w where w.user.id = :userId")
    Optional<PointWallet> findByUserIdForUpdate(@Param("userId") Long userId);
}
