package com.tg.jackysdailychallenge.component;

public enum Command {
    START("start"),
    ADD_CHALLENGE("add_challenge"),
    REMOVE_CHALLENGE("remove_challenge"),
    LIST_CHALLENGES("list_challenges"),
    DRAW_DAILY_CHALLENGE("draw_daily_challenge"),
    DRAW_RANDOM_CHALLENGE_FROM_ALL("draw_random_challenge"),
    SHOW_DAILY_CHALLENGE("show_daily_challenge"),
    COMPLETE_DAILY_CHALLENGE("complete_daily_challenge"),
    SCORE("score"),
    SUMMARY("summary"),
    RESET("reset");

    private String cmd;

    Command(String cmd) {
        this.cmd = cmd;
    }

    public String cmd() {
        return cmd;
    }
}
