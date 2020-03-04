package com.tg.jackysdailychallenge.component;

import com.google.common.collect.Lists;
import com.tg.jackysdailychallenge.model.Challenge;
import com.tg.jackysdailychallenge.service.ChallengeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
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
    private ChallengeService challengeService;

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
        System.out.println(String.format("@User#%d %s: %s", fromUser.getId(), fromUser.getFirstName(), cmd));

        SendMessage outMsg = new SendMessage();
        setReplyMsgFrm(update.getMessage(), outMsg);


        switch (cmd) {
            case "start":
                outMsg.setText("add later...");
                break;
            case "add_challenge":
                outMsg.setText("add later...");
                break;
            case "list_challenges":
                listChallenges(outMsg);
                break;
            case "draw_daily_challenge":
                drawDailyChallenge(outMsg);
                break;
            case "score":
                outMsg.setText("add later...");
                break;
            case "summary":
                outMsg.setText("add later...");
                break;
            case "reset":
                outMsg.setText("add later...");
                break;
            default:
                outMsg.setText("add later...");
        }

        try {
            execute(outMsg);
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

    private void setReplyMsgFrm(Message inMsg, SendMessage outMsg) {
        outMsg.setChatId(inMsg.getChatId());
        outMsg.setReplyToMessageId(inMsg.getMessageId());
    }

    private void listChallenges(SendMessage outMsg) {
        List<Challenge> allChallenges = challengeService.findAll();
        AtomicInteger index = new AtomicInteger(1);

        List<String> tempAllChallenges = allChallenges.stream()
            .map(Challenge::getTitle).sorted()
            .map(title -> String.format("%d. %s", index.getAndIncrement(), title))
            .collect(Collectors.toList());

        outMsg.setText(allChallenges.isEmpty() ? "There are no challenges yet T.T" : String.join("\n", tempAllChallenges));
    }

    private void drawDailyChallenge(SendMessage outMsg) {
        List<Challenge> shuffledChallenges = Lists.newArrayList(challengeService.findAll());
        Collections.shuffle(shuffledChallenges);
        outMsg.setText(shuffledChallenges.isEmpty() ? "There are no challenges yet T.T" : shuffledChallenges.get(0).getTitle());
    }
}
