package com.example.predict.prediction.repository;

import com.example.predict.match.domain.TeamSide;
import com.example.predict.prediction.domain.Prediction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PredictionRepository extends JpaRepository<Prediction, Long> {

    boolean existsByUserIdAndMatchId(Long userId, Long matchId);

    Optional<Prediction> findByUserIdAndMatchId(Long userId, Long matchId);

    List<Prediction> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<Prediction> findByMatchId(Long matchId);

    @Query("select coalesce(sum(p.betPoint), 0) from Prediction p where p.match.id = :matchId")
    long sumBetPointByMatchId(@Param("matchId") Long matchId);

    @Query("select coalesce(sum(p.betPoint), 0) from Prediction p where p.match.id = :matchId and p.selectedTeam = :team")
    long sumBetPointByMatchIdAndTeam(@Param("matchId") Long matchId, @Param("team") TeamSide team);
}
