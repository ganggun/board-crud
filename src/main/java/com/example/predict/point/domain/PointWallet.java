package com.example.predict.point.domain;

import com.example.predict.user.domain.User;
import jakarta.persistence.*;

@Entity
@Table(name = "point_wallets")
public class PointWallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private long point;

    protected PointWallet() {
    }

    public PointWallet(User user, long point) {
        this.user = user;
        this.point = point;
    }

    public void withdraw(long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("포인트는 1 이상이어야 합니다.");
        }
        if (point < amount) {
            throw new IllegalArgumentException("포인트가 부족합니다.");
        }
        point -= amount;
    }

    public void deposit(long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("포인트는 1 이상이어야 합니다.");
        }
        point += amount;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public long getPoint() {
        return point;
    }
}
