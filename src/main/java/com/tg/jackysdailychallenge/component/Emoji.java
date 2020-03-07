package com.tg.jackysdailychallenge.component;

public enum Emoji {
    CRY("\uD83D\uDE2D"),
    ADD_OIL("\uD83D\uDCAA"),
    HAPPY("\uD83D\uDE03"),
    COMPLETE("✅"),
    THUMBS_UP("\uD83D\uDC4D"),
    CLAP("\uD83D\uDC4F"),
    WINKY("\uD83D\uDE09");


    private String unicode;

    Emoji(String unicode) {
        this.unicode = unicode;
    }

    public String getCode() {
        return unicode;
    }
}
