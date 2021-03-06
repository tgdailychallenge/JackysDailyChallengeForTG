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

    public Challenge(String title, boolean isWeekendOnly) {
        setTitle(title);
        setScore(100);
        setWeekendOnly(isWeekendOnly);
    }

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

    public boolean getIsWeekendOnly() {
        return isWeekendOnly;
    }

    public void setWeekendOnly(boolean weekendOnly) {
        isWeekendOnly = weekendOnly;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Challenge challenge = (Challenge) obj;
        return title.equals(challenge.getTitle()) && score == challenge.getScore() && isWeekendOnly == challenge.getIsWeekendOnly();
    }
}
