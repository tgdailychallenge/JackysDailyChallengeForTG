package com.tg.jackysdailychallenge.service;

import com.tg.jackysdailychallenge.model.Challenge;
import com.tg.jackysdailychallenge.model.Challenger;

import java.util.List;
import java.util.Optional;

public interface ChallengerService {
    List<Challenger> findAll();
    Optional<Challenger> findByUserId(int userId);
    Challenger insertChallenger(Challenger challenger);
    Challenger updateChallenger(Challenger challenger);
    void removeChallenger(Challenger challenger);
    void removeChallengerByUserId(int userId);

    Optional<List<Challenge>> findChallengeListByUserId(int userId);
    Optional<Challenge> findChallengeByUserIdAndChallengeTitle(int userId, String title);
    void insertToChallengeListByUserId(int userId, Challenge challenge);
    void updateChallengeByUserIdAndChallengeTitle(int userId, String title, Challenge challenge);
    void removeFromChallengeListByUserIdAndChallengeTitle(int userId, String title);
    Optional<Challenge> findDailyChallengeById(int userId);
    void updateDailyChallengeAndDateById(int userId, Challenge dailyChallenge);
    int findScoreById(int userId);
    void updateScoreById(int userId, int score);
}
