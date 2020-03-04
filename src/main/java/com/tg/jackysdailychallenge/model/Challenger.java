package com.tg.jackysdailychallenge.model;

import com.google.common.collect.Lists;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.List;

public class Challenger {
    @Id private ObjectId _id;

    private int userId;
    private int score;
    private Challenge dailyChallenge;
    private Date challengeDate;
    private List<Challenge> challengeList;


    public Challenger(int userId) {
        setUserId(userId);
        setScore(0);
        setDailyChallenge(null);
        setChallengeDate(null);
        setChallengeList(Lists.newArrayList());
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Challenge getDailyChallenge() {
        return dailyChallenge;
    }

    public void setDailyChallenge(Challenge dailyChallenge) {
        this.dailyChallenge = dailyChallenge;
    }

    public Date getChallengeDate() {
        return challengeDate;
    }

    public void setChallengeDate(Date challengeDate) {
        this.challengeDate = challengeDate;
    }

    public List<Challenge> getChallengeList() {
        return challengeList;
    }

    public void setChallengeList(List<Challenge> challengeList) {
        this.challengeList = challengeList;
    }
}
