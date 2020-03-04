package com.tg.jackysdailychallenge.component;

import com.google.common.collect.Lists;
import com.tg.jackysdailychallenge.model.Challenge;
import com.tg.jackysdailychallenge.model.Challenger;
import com.tg.jackysdailychallenge.repository.ChallengerRepository;
import com.tg.jackysdailychallenge.service.ChallengeService;
import com.tg.jackysdailychallenge.service.ChallengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class JackysdailychallengeBotComponent extends TelegramLongPollingBot {
    @Autowired
    private ChallengeService challengeService;

    @Autowired
    private ChallengerService challengerService;

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
        if (Optional.ofNullable(update.getCallbackQuery()).isPresent()) {
            onCallbackCommand(update.getCallbackQuery());
        } else
            onUserCommand(update.getMessage());
    }

    @Override
    public String getBotUsername() {
        return "jackys_daily_challenge_bot";
    }

    @Override
    public String getBotToken() {
        return "803922992:AAF3wRVgvo8veraS9PK-KGqg-sTz8vhoG1s";
    }

    private void onUserCommand(Message inMsg) {
        System.out.println("From user cmd...");
        User fromUser = inMsg.getFrom();
        String cmd = getCommand(inMsg.getText());
        System.out.println(String.format("@User#%d %s: %s", fromUser.getId(), fromUser.getFirstName(), cmd));

        SendMessage outMsg = new SendMessage()
            .setChatId(inMsg.getChatId())
            .setReplyToMessageId(inMsg.getMessageId());
        executeCommand(outMsg, cmd, fromUser);
    }

    private void onCallbackCommand(CallbackQuery inQuery) {
        System.out.println("From callback query...");
        User fromUser = inQuery.getFrom();
        String cmd = inQuery.getData();
        System.out.println(String.format("@User#%d %s: %s", fromUser.getId(), fromUser.getFirstName(), cmd));

        SendMessage outMsg = new SendMessage()
            .setChatId(inQuery.getMessage().getChatId());
        executeCommand(outMsg, cmd, fromUser);
    }

    private void executeCommand(SendMessage outMsg, String cmd, User fromUser) {
        switch (cmd) {
            case "start":
                start(fromUser.getId(), outMsg);
                break;
            case "add_challenge":
                outMsg.setText("add later...");
                break;
            case "list_challenges":
                listChallenges(fromUser.getId(), outMsg);
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
        outMsg.setText(String.format("@%s\n%s", Optional.ofNullable(fromUser.getUserName()).orElse(fromUser.getFirstName()), outMsg.getText()));

        try {
            execute(outMsg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private String getCommand(String cmd) {
        return cmd.split("/")[1]
            .split("@")[0];
    }

    private void start(int userId, SendMessage outMsg) {
        if (!challengerService.findByUserId(userId).isPresent())
            challengerService.insertChallenger(new Challenger(userId));

        List<List<InlineKeyboardButton>> inlineKeyboard = Arrays.asList(Arrays.asList(
            new InlineKeyboardButton()
                .setText("Add Challenge")
                .setCallbackData("add_challenge"),
            new InlineKeyboardButton()
                .setText("List Challenges")
                .setCallbackData("list_challenges")
        ));
        outMsg.setText("You are all set. Please choose the following actions:")
            .setReplyMarkup(
            new InlineKeyboardMarkup()
                .setKeyboard(inlineKeyboard));
    }

    private void listChallenges(int userId, SendMessage outMsg) {
        List<Challenge> allChallenges = challengerService.findChallengeListByUserId(userId).orElse(Lists.newArrayList());
        if (!allChallenges.isEmpty()) {
            AtomicInteger index = new AtomicInteger(1);
            List<String> tempAllChallenges = allChallenges.stream()
                .map(Challenge::getTitle).sorted()
                .map(title -> String.format("%d. %s", index.getAndIncrement(), title))
                .collect(Collectors.toList());

            outMsg.setText(String.join("\n", tempAllChallenges));
        } else
            outMsg.setText("There are no challenges yet T.T");
    }

    private void drawDailyChallenge(SendMessage outMsg) {
        List<Challenge> shuffledChallenges = Lists.newArrayList(challengeService.findAll());
        Collections.shuffle(shuffledChallenges);
        outMsg.setText(shuffledChallenges.isEmpty() ? "There are no challenges yet T.T" : shuffledChallenges.get(0).getTitle());
    }
}
