package com.example.predict.match.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "matches")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String teamA;

    @Column(nullable = false)
    private String teamB;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MatchStatus status = MatchStatus.SCHEDULED;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private TeamSide winnerTeam;

    protected Match() {
    }

    public Match(String title, String category, String teamA, String teamB, LocalDateTime startTime, MatchStatus status) {
        this.title = title;
        this.category = category;
        this.teamA = teamA;
        this.teamB = teamB;
        this.startTime = startTime;
        this.status = status;
    }

    public void update(String title, String category, String teamA, String teamB, LocalDateTime startTime, MatchStatus status) {
        this.title = title;
        this.category = category;
        this.teamA = teamA;
        this.teamB = teamB;
        this.startTime = startTime;
        this.status = status;
    }

    public void finish(TeamSide winnerTeam) {
        this.winnerTeam = winnerTeam;
        this.status = MatchStatus.FINISHED;
    }

    public boolean canPredict(LocalDateTime now) {
        return status == MatchStatus.PREDICTING && now.isBefore(startTime);
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getTeamA() {
        return teamA;
    }

    public String getTeamB() {
        return teamB;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public MatchStatus getStatus() {
        return status;
    }

    public TeamSide getWinnerTeam() {
        return winnerTeam;
    }
}
