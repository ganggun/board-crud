package com.example.predict.point.domain;

import com.example.predict.match.domain.Match;
import com.example.predict.prediction.domain.Prediction;
import com.example.predict.user.domain.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "point_transactions")
public class PointTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id")
    private Match match;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prediction_id")
    private Prediction prediction;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private PointTransactionType type;

    @Column(nullable = false)
    private long amount;

    @Column(nullable = false)
    private long balanceAfter;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected PointTransaction() {
    }

    public PointTransaction(User user, Match match, Prediction prediction,
                            PointTransactionType type, long amount, long balanceAfter) {
        this.user = user;
        this.match = match;
        this.prediction = prediction;
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
    }

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
