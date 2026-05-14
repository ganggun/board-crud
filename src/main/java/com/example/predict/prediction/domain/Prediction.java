package com.example.predict.prediction.domain;

import com.example.predict.match.domain.Match;
import com.example.predict.match.domain.TeamSide;
import com.example.predict.user.domain.User;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "predictions",
        uniqueConstraints = @UniqueConstraint(name = "uk_prediction_user_match", columnNames = {"user_id", "match_id"})
)
public class Prediction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private TeamSide selectedTeam;

    @Column(nullable = false)
    private long betPoint;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal odds;

    @Column(nullable = false)
    private long expectedRewardPoint;

    private Long rewardPoint;

    private Boolean correct;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected Prediction() {
    }

    public Prediction(User user, Match match, TeamSide selectedTeam,
                      long betPoint, BigDecimal odds, long expectedRewardPoint) {
        this.user = user;
        this.match = match;
        this.selectedTeam = selectedTeam;
        this.betPoint = betPoint;
        this.odds = odds;
        this.expectedRewardPoint = expectedRewardPoint;
    }

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public void settle(boolean correct, long rewardPoint) {
        this.correct = correct;
        this.rewardPoint = rewardPoint;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Match getMatch() {
        return match;
    }

    public TeamSide getSelectedTeam() {
        return selectedTeam;
    }

    public long getBetPoint() {
        return betPoint;
    }

    public BigDecimal getOdds() {
        return odds;
    }

    public long getExpectedRewardPoint() {
        return expectedRewardPoint;
    }

    public Long getRewardPoint() {
        return rewardPoint;
    }

    public Boolean getCorrect() {
        return correct;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
