package com.tg.jackysdailychallenge.service;

import com.tg.jackysdailychallenge.model.Challenge;
import com.tg.jackysdailychallenge.repository.ChallengeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChallengeServiceImpl implements ChallengeServicable {
    @Autowired
    private ChallengeRepository challengeRepository;


    @Override
    public List<Challenge> findAll() {
        return challengeRepository.findAll();
    }

    @Override
    public List<Challenge> findByTitle(String title) {
        return challengeRepository.findByTitle(title);
    }

    @Override
    public List<Challenge> findByScore(int score) {
        return challengeRepository.findByScore(score);
    }

    @Override
    public List<Challenge> findByIsWeekendOnly(boolean isWeekendOnly) {
        return challengeRepository.findByIsWeekendOnly(isWeekendOnly);
    }


    @Override
    public List<Challenge> insertChallenge(Challenge challenge) {
        challengeRepository.insert(challenge);
        return findAll();
    }

    @Override
    public List<Challenge> deleteChallenge(Challenge challenge) {
        challengeRepository.delete(challenge);
        return findAll();
    }
}
