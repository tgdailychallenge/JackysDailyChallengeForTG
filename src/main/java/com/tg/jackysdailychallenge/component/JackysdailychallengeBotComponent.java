package com.tg.jackysdailychallenge.component;

import com.google.common.collect.Lists;
import com.tg.jackysdailychallenge.model.Challenge;
import com.tg.jackysdailychallenge.model.Challenger;
import com.tg.jackysdailychallenge.service.ChallengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ForceReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class JackysdailychallengeBotComponent extends TelegramLongPollingBot {

    @Autowired
    private ChallengerService challengerService;


    @Override
    public void onUpdateReceived(Update update) {
        if (Optional.ofNullable(update.getCallbackQuery()).isPresent() && "JackysDailyChallengeBot".equals(update.getCallbackQuery().getMessage().getFrom().getUserName()))
            onCallbackCommand(update.getCallbackQuery());
        else
            onUserInput(update.getMessage());
    }

    @Override
    public String getBotUsername() {
        return "jackys_daily_challenge_bot";
    }

    @Override
    public String getBotToken() {
        return "803922992:AAF3wRVgvo8veraS9PK-KGqg-sTz8vhoG1s";
    }

    private void onUserInput(Message inMsg) {
        User fromUser = inMsg.getFrom();
        System.out.println(String.format("@User#%d %s: %s", fromUser.getId(), fromUser.getFirstName(), inMsg.getText()));

        SendMessage outMsg = new SendMessage()
            .setChatId(inMsg.getChatId())
            .setReplyToMessageId(inMsg.getMessageId())
            .setText(String.format("@%s", Optional.ofNullable(fromUser.getUserName()).orElse(fromUser.getFirstName())));

        if (isValidUserCommand(inMsg.getText()))
            onUserCommand(inMsg, fromUser, outMsg);
        else if (isAddChallengeTitle(inMsg)) {
            addChallengeAskForIsWeekendOnly(inMsg, fromUser, outMsg);
        } else {
            System.out.println("None of my business");
        }

    }

    private boolean isAddChallengeTitle(Message inMsg) {
        return inMsg.getReplyToMessage() != null && "JackysDailyChallengeBot".equals(inMsg.getReplyToMessage().getFrom().getUserName()) && inMsg.getReplyToMessage().getText().contains("Please send me your new challenge.");
    }

    private boolean isAddChallengeIsWeekendOnly(CallbackQuery inQuery) {
//        System.out.println(inQuery.getMessage().getReplyToMessage() != null);
        System.out.println(inQuery.getMessage().getText());
        return inQuery.getMessage().getReplyToMessage() != null && inQuery.getMessage().getText().contains("for weekend only?");
    }

    private void onUserCommand(Message inMsg, User fromUser, SendMessage outMsg) {
        String cmd = getCommand(inMsg.getText());
        System.out.println(String.format("Command: %s", cmd));

        executeCommand(outMsg, cmd, fromUser);
    }

    private void onCallbackCommand(CallbackQuery inQuery) {
        System.out.println("From callback query...");
        User fromUser = inQuery.getFrom();
        String cmd = inQuery.getData();
        System.out.println(String.format("@User#%d %s: %s", fromUser.getId(), fromUser.getFirstName(), cmd));

        SendMessage outMsg = new SendMessage()
            .setChatId(inQuery.getMessage().getChatId())
            .setText(String.format("@%s", Optional.ofNullable(fromUser.getUserName()).orElse(fromUser.getFirstName())));

        if (isAddChallengeIsWeekendOnly(inQuery)) {
            outMsg.setReplyToMessageId(inQuery.getMessage().getMessageId());

            String title = inQuery.getMessage().getReplyToMessage().getText();
            boolean isWeekendOnly = Boolean.valueOf(inQuery.getData());
            System.out.println(String.format("Add new challenge: %s", title));
            System.out.println(String.format("IsWeekendOnly: %b", isWeekendOnly));

            addNewChallengeToChallenger(
                fromUser.getId(),
                new Challenge(title, isWeekendOnly),
                outMsg
            );

            try {
                execute(outMsg);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else
            executeCommand(outMsg, cmd, fromUser);

    }

    private void executeCommand(SendMessage outMsg, String cmd, User fromUser) {
        switch (stringToCommand(cmd)) {
            case START:
                start(fromUser.getId(), outMsg);
                setDefaultInlineKeyboard(outMsg);
                break;
            case ADD_CHALLENGE:
                addChallengeAskForTitle(fromUser.getId(), outMsg);
                break;
            case LIST_CHALLENGES:
                listChallenges(fromUser.getId(), outMsg);
                setDefaultInlineKeyboard(outMsg);
                break;
            case DRAW_DAILY_CHALLENGE:
                drawDailyChallenge(fromUser.getId(), outMsg);
                setDefaultInlineKeyboard(outMsg);
                break;
            case SCORE:
                showScore(fromUser.getId(), outMsg);
                setDefaultInlineKeyboard(outMsg);
                break;
            case RESET:
                outMsg.setText("add later...");
                setDefaultInlineKeyboard(outMsg);
                break;
        }

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

        appendTextToMsg("You are all set. Please choose the following actions:", outMsg);
    }

    private void setDefaultInlineKeyboard(SendMessage outMsg) {
        List<List<InlineKeyboardButton>> inlineKeyboard = Arrays.asList(
            Arrays.asList(
                new InlineKeyboardButton()
                    .setText("Add Challenge")
                    .setCallbackData("add_challenge"),
                new InlineKeyboardButton()
                    .setText("List Challenges")
                    .setCallbackData("list_challenges")
            ),
            Arrays.asList(
                new InlineKeyboardButton()
                    .setText("Draw Daily Challenge")
                    .setCallbackData("draw_daily_challenge"),
                new InlineKeyboardButton()
                    .setText("Show Score")
                    .setCallbackData("score")
            ));
        outMsg.setReplyMarkup(
            new InlineKeyboardMarkup()
                .setKeyboard(inlineKeyboard));
    }

    private void listChallenges(int userId, SendMessage outMsg) {
        List<Challenge> allChallenges = challengerService.findChallengeListByUserId(userId).orElse(Lists.newArrayList());
        if (!allChallenges.isEmpty()) {
            AtomicInteger index = new AtomicInteger(1);

            appendTextToMsg("Here are your existing challenge list:", outMsg);
            allChallenges.stream()
                .forEach(challenge ->
                    appendTextToMsg(
                        String.format("%d. %s %s",
                            index.getAndIncrement(),
                            challenge.getTitle(),
                            challenge.getIsWeekendOnly() ? "(Weekend only)" : ""
                        ), outMsg)
                );
        } else
            appendTextToMsg("You have no challenges yet T.T", outMsg);
    }

    private void drawDailyChallenge(int userId, SendMessage outMsg) {
        List<Challenge> shuffledChallenges = challengerService.findChallengeListByUserId(userId).orElse(Lists.newArrayList());
        Collections.shuffle(shuffledChallenges);
        if (shuffledChallenges.isEmpty()) {
            appendTextToMsg("You have no challenges yet... T.T", outMsg);
            return;
        }

        if (isReadyToDrawDailyChallenge(userId)) {
            Challenge dailyChallenge = shuffledChallenges.get(0);
            challengerService.updateDailyChallengeAndDateById(userId, dailyChallenge);
        } else {
            appendTextToMsg("You have already draw your daily challenge today! Please draw again tmr!", outMsg);
        }

        Challenge dailyChallenge = challengerService.findByUserId(userId).get().getDailyChallenge();
        appendTextToMsg(String.format("Complete the following challenge to gain %d points:", dailyChallenge.getScore()), outMsg);
        appendTextToMsg(dailyChallenge.getTitle(), outMsg);
    }

    private void addChallengeAskForTitle(int userId, SendMessage outMsg) {
        listChallenges(userId, outMsg);
        appendTextToMsg("Please send me your new challenge.", outMsg);
        outMsg.setReplyMarkup(
            new ForceReplyKeyboard()
                .setSelective(true)
        );
    }

    private void addChallengeAskForIsWeekendOnly(Message inMsg, User fromUser, SendMessage outMsg) {
        List<List<InlineKeyboardButton>> inlineKeyboard = Arrays.asList(Arrays.asList(
            new InlineKeyboardButton()
                .setText("Yes")
                .setCallbackData("true"),
            new InlineKeyboardButton()
                .setText("No")
                .setCallbackData("false")
        ));
        outMsg.setText(String.format("@%s", Optional.ofNullable(fromUser.getUserName()).orElse(fromUser.getFirstName())))
            .setReplyMarkup(
                new InlineKeyboardMarkup()
                    .setKeyboard(inlineKeyboard));

        appendTextToMsg(String.format("Is \"%s\" for weekend only?", inMsg.getText()), outMsg);

        try {
            execute(outMsg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void addNewChallengeToChallenger(int userId, Challenge newChallenge, SendMessage outMsg) {
        challengerService.insertToChallengeListByUserId(userId, newChallenge);
        listChallenges(userId, outMsg);
        setDefaultInlineKeyboard(outMsg);
    }

    private void showScore(int userId, SendMessage outMsg) {
        appendTextToMsg(String.format("You now have %d points.",
            challengerService.findScoreById(userId)), outMsg);
    }

    private void appendTextToMsg(String text, SendMessage outMsg) {
        outMsg.setText(String.format("%s\n%s", outMsg.getText(), text));
    }

    private boolean isValidUserCommand(String text) {
        if (!text.contains("/"))
            return false;

        return !getCommandByText(text.split("/")[1])
            .isEmpty();
    }

    private Command stringToCommand(String text) {
        System.out.println(getCommandByText(text)
            .toString());

        return getCommandByText(text)
            .get(0);
    }

    private List<Command> getCommandByText(String text) {
        return Arrays.stream(Command.values())
            .filter(command -> text.equals(command.cmd()))
            .collect(Collectors.toList());
    }

    private boolean isReadyToDrawDailyChallenge(int userId) {
        if (challengerService.findByUserId(userId).get().getChallengeDate() == null)
            return true;

        LocalDate challengeDate = challengerService.findByUserId(userId).get().getChallengeDate()
            .toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate todayDate = Calendar.getInstance().getTime()
            .toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        return !challengeDate.isEqual(todayDate);
    }
}
