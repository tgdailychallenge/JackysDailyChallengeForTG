package com.tg.jackysdailychallenge;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
public class JackysdailychallengeBotComponent extends TelegramLongPollingBot {
    private List<String> allChallenges = Lists.newArrayList(
            "Play guitar / Play ukulele",
            "30 min training",
            "Reading",
            "Programming / Vege day",
            "Stretching",
            "Drink 6 cups of water today"
    );

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
            case "list_challenges":
                Collections.sort(allChallenges);
                List<String> tempAllChallenges = Lists.newArrayList();
                AtomicInteger index = new AtomicInteger(1);
                allChallenges.stream().forEach(challenge -> tempAllChallenges.add(String.format("%d. %s", index.getAndIncrement(), challenge)));
                text = allChallenges.isEmpty() ? "There are no challenges yet T.T" : String.join("\n", tempAllChallenges);
                break;
            case "draw_daily_challenge":
                List<String> shuffledChallenges = Lists.newArrayList(allChallenges);
                Collections.shuffle(shuffledChallenges);
                text = shuffledChallenges.get(0);
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
}
