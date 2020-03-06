package com.tg.jackysdailychallenge.service;

import com.tg.jackysdailychallenge.model.Challenge;
import com.tg.jackysdailychallenge.model.Challenger;
import com.tg.jackysdailychallenge.repository.ChallengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        findByUserId(userId).ifPresent(challenger ->
            challenger.setChallengeList(
                    challenger.getChallengeList().stream()
                            .filter(challenge1 -> !title.equals(challenge1.getTitle())).collect(Collectors.toList()))
        );
    }

    @Override
    public void updateDailyChallengeAndDateByUserId(int userId, Challenge dailyChallenge) {
        findByUserId(userId).ifPresent(challenger -> {
            challenger.setDailyChallenge(dailyChallenge);
            challenger.setChallengeDate(new Date());
            challengerRepository.save(challenger);
        });
    }
}
