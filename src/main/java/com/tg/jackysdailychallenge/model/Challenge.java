package com.tg.jackysdailychallenge.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "challenges")
public class Challenge {
    @Id private ObjectId _id;

    private String title;
    private int score;
    private boolean isWeekendOnly;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isWeekendOnly() {
        return isWeekendOnly;
    }

    public void setWeekendOnly(boolean weekendOnly) {
        isWeekendOnly = weekendOnly;
    }
}
