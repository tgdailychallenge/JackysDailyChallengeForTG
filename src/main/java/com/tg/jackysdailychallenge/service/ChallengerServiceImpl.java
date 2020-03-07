package com.tg.jackysdailychallenge.service;

import com.tg.jackysdailychallenge.model.Challenge;
import com.tg.jackysdailychallenge.model.Challenger;
import com.tg.jackysdailychallenge.repository.ChallengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChallengerServiceImpl implements ChallengerService {
    @Autowired
    ChallengerRepository challengerRepository;

    @Override
    public List<Challenger> findAll() {
        return challengerRepository.findAll();
    }

    @Override
    public Optional<Challenger> findByUserId(int userId) {
        return challengerRepository.findByUserId(userId).stream().findFirst();
    }

    @Override
    public Challenger insertChallenger(Challenger challenger) {
        return challengerRepository.insert(challenger);
    }

    @Override
    public Challenger updateChallenger(Challenger challenger) {
        return challengerRepository.save(challenger);
    }

    @Override
    public void removeChallenger(Challenger challenger) {
        challengerRepository.delete(challenger);
    }

    @Override
    public void removeChallengerByUserId(int userId) {
        findByUserId(userId).ifPresent(challenger -> challengerRepository.delete(challenger));
    }

    @Override
    public Optional<List<Challenge>> findChallengeListByUserId(int userId) {
        return Optional.ofNullable(findByUserId(userId).get().getChallengeList());
    }

    @Override
    public Optional<Challenge> findChallengeByUserIdAndChallengeTitle(int userId, String title) {
        return findChallengeListByUserId(userId).get().stream().filter(challenge -> title.equals(challenge.getTitle())).findFirst();
    }

    @Override
    public List<Challenge> findAllChallengeList() {
        return challengerRepository.findAll().stream()
            .map(Challenger::getChallengeList)
            .flatMap(Collection::stream)
            .distinct()
            .collect(Collectors.toList());
    }

    @Override
    public void insertToChallengeListByUserId(int userId, Challenge challenge) {
        findByUserId(userId).ifPresent(challenger -> {
            challenger.getChallengeList().add(challenge);
            challengerRepository.save(challenger);
        });
    }

    @Override
    public void updateChallengeByUserIdAndChallengeTitle(int userId, String title, Challenge challenge) {
        removeFromChallengeListByUserIdAndChallengeTitle(userId, title);
        insertToChallengeListByUserId(userId, challenge);
    }

    @Override
    public void removeFromChallengeListByUserIdAndChallengeTitle(int userId, String title) {
        findByUserId(userId).ifPresent(challenger -> {
            challenger.setChallengeList(
                challenger.getChallengeList().stream()
                    .filter(challenge1 -> !title.equals(challenge1.getTitle())).collect(Collectors.toList()));
            updateChallenger(challenger);
        });
    }

    @Override
    public void updateDailyChallengeAndDateById(int userId, Challenge dailyChallenge) {
        findByUserId(userId).ifPresent(challenger -> {
            challenger.setDailyChallenge(dailyChallenge);
            challenger.setChallengeDate(new Date());
            challengerRepository.save(challenger);
        });
    }

    @Override
    public void updateDailyChallengeById(int userId, Challenge dailyChallenge) {
        findByUserId(userId).ifPresent(challenger -> {
            challenger.setDailyChallenge(dailyChallenge);
            challengerRepository.save(challenger);
        });
    }

    @Override
    public int findScoreById(int userId) {
        return findByUserId(userId)
            .map(Challenger::getScore)
            .orElse(0);
    }

    @Override
    public void updateScoreById(int userId, int score) {
        findByUserId(userId).ifPresent(challenger -> {
            challenger.setScore(score);
            challengerRepository.save(challenger);
        });
    }

    @Override
    public Optional<Challenge> findDailyChallengeById(int userId) {
        return findByUserId(userId)
            .map(Challenger::getDailyChallenge);
    }

    @Override
    public boolean findCompleteById(int userId) {
        return findByUserId(userId)
            .map(Challenger::isComplete)
            .orElse(false);
    }

    @Override
    public void updateCompleteById(int userId, boolean complete) {
        findByUserId(userId).ifPresent(challenger -> {
            challenger.setComplete(complete);
            challengerRepository.save(challenger);
        });
    }
}
