package com.tg.jackysdailychallenge.service;

import com.tg.jackysdailychallenge.model.Challenge;

import java.util.List;

public interface ChallengeServicable {
    List<Challenge> findAll();
    List<Challenge> findByTitle(String title);
    List<Challenge> findByScore(int score);
    List<Challenge> findByIsWeekendOnly(boolean isWeekendOnly);
    List<Challenge> insertChallenge(Challenge challenge);
    List<Challenge> deleteChallenge(Challenge challenge);
//    List<Challenge> updateChallenge(Challenge oldChallenge, Challenge newChallenge);
}
