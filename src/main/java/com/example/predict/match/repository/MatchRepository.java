package com.example.predict.match.repository;

import com.example.predict.match.domain.Match;
import com.example.predict.match.domain.MatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {

    List<Match> findByStatusInOrderByStartTimeAsc(Collection<MatchStatus> statuses);
}
