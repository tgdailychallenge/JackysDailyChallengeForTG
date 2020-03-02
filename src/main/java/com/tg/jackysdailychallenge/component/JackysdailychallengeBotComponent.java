package com.tg.jackysdailychallenge.component;

import com.google.common.collect.Lists;
import com.tg.jackysdailychallenge.model.Challenge;
import com.tg.jackysdailychallenge.service.ChallengeServicable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class JackysdailychallengeBotComponent extends TelegramLongPollingBot {
    @Autowired
    private ChallengeServicable challengeService;

//    private List<Challenge> allChallenges = challengeService.findAll();

//    private List<String> allChallenges = Lists.newArrayList(
//            "Play guitar / Play ukulele",
//            "30 min training",
//            "Reading",
//            "Programming / Vege day",
//            "Stretching",
//            "Drink 6 cups of water today"
//    );

    @Override
    public void onUpdateReceived(Update update) {
        User fromUser = update.getMessage().getFrom();
        String cmd = getCommand(update.getMessage().getText());
        System.out.println(cmd);

        SendMessage msg = new SendMessage();
        String text;


        switch (cmd) {
            case "start":
                text = "this bot is running";
                break;
            case "add_challenge":
                text = "add later...";
                break;
            case "list_challenges":
                text = listChallenges();
                break;
            case "draw_daily_challenge":
                text = drawDailyChallenge();
                break;
            case "score":
                text = "add later...";
                break;
            case "summary":
                text = "add later...";
                break;
            case "reset":
                text = "add later...";
                break;
            default:
                text = "this bot is running";
        }

        msg.setChatId(update.getMessage().getChatId());
        msg.setText(text);
        msg.setReplyToMessageId(update.getMessage().getMessageId());
        try {
            execute(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String getBotUsername() {
        return "jackys_daily_challenge_bot";
    }

    @Override
    public String getBotToken() {
        return "803922992:AAF3wRVgvo8veraS9PK-KGqg-sTz8vhoG1s";
    }

    private String getCommand(String cmd) {
        return cmd.split("/")[1]
                .split("@")[0];
    }

    private String listChallenges() {
        List<Challenge> allChallenges = challengeService.findAll();
        AtomicInteger index = new AtomicInteger(1);

        List<String> tempAllChallenges = allChallenges.stream()
            .map(Challenge::getTitle).sorted()
            .map(title -> String.format("%d. %s", index.getAndIncrement(), title))
            .collect(Collectors.toList());

        return allChallenges.isEmpty() ? "There are no challenges yet T.T" : String.join("\n", tempAllChallenges);
    }

    private String drawDailyChallenge() {
        List<Challenge> shuffledChallenges = Lists.newArrayList(challengeService.findAll());
        Collections.shuffle(shuffledChallenges);
        return shuffledChallenges.isEmpty() ? "There are no challenges yet T.T" : shuffledChallenges.get(0).getTitle();
    }
}
