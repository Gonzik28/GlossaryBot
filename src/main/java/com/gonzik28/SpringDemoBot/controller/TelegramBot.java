package com.gonzik28.SpringDemoBot.controller;

import com.gonzik28.SpringDemoBot.config.BotConfig;

import com.gonzik28.SpringDemoBot.dto.*;
import com.gonzik28.SpringDemoBot.service.GlossaryService;
import com.gonzik28.SpringDemoBot.service.LevelOfStudyService;
import com.gonzik28.SpringDemoBot.service.StudyOptionsService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.polls.Poll;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll;

import java.util.*;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    final BotConfig config;
    private final GlossaryService glossaryService;
    private final LevelOfStudyService levelOfStudyService;
    private final StudyOptionsService studyOptionsService;
    private final int MAX_QUESTIONS = 3;
    private static final String HELP_TEXT = "This bot is creating to English glossary.\n"
            + "Type /start too see a welcome message \n"
            + "Type /time change time quiz \n"
            + "Type /teach too see a quiz message \n"
            + "Type /exit too see a exit message";

    public TelegramBot(BotConfig config, LevelOfStudyService levelOfStudyService,
                       GlossaryService glossaryService, StudyOptionsService studyOptionsService) {
        this.config = config;
        this.glossaryService = glossaryService;
        this.levelOfStudyService = levelOfStudyService;
        this.studyOptionsService = studyOptionsService;
        try {
            this.execute(Options.generatorMenu());
        } catch (TelegramApiException e) {
            e.getMessage();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            String firstName = update.getMessage().getChat().getFirstName();
            String userName = update.getMessage().getChat().getUserName();
            try {
                if (levelOfStudyService.findByUserName(userName) != null &&
                        studyOptionsService.findByUserName(userName).isStudy()) {
                    Integer num = Integer.parseInt(messageText);
                    if (num > 0) {
                        levelOfStudyService.update(Options.updateTimeFalse(userName, num));
                        studyOptionsService.updateStudy(userName, false, null);
                        messageText = "/teach";
                    } else {
                        messageText = "/time";
                    }
                }
            } catch (NoSuchElementException | NumberFormatException e) {
                e.getMessage();
            }
            switch (messageText) {
                case "/start":
                    startCommandReceived(chatId, firstName, userName);
                    break;
                case "A0":
                case "A1":
                case "A2":
                case "B1":
                case "B2":
                case "C1":
                case "C2":
                    levelCommandReceived(chatId, userName, messageText);
                    break;
                case "/time":
                    if (studyOptionsService.findByUserName(userName) != null) {
                        studyOptionsService.updateStudy(userName, true, System.currentTimeMillis());
                    } else {
                        studyOptionsService.create(Options.pollSetNull(chatId, userName, true));
                    }
                    timeCommandReceived(chatId);
                    break;
                case "/teach":
                    studyOptionsService.updateStudy(userName, true, System.currentTimeMillis());
                    teachCommandReceived(chatId, userName);
                    break;
                case "/exit":
                    studyOptionsService.updateStudy(userName, false, null);
                    exitCommandReceived(chatId, firstName);
                    studyOptionsService.updatePoll(userName, null);
                    break;
                case "/help":
                    sendMessage(chatId, HELP_TEXT);
                    break;
                default:
                    sendMessage(chatId, "Sorry, command was not recognized");
            }
        } else if (update.getPoll() != null && studyOptionsService.findByPollId(update.getPoll().getId())!=null){
            ResponseStudyOptionsDto responseStudyOptionsDto =
                    studyOptionsService.findByPollId(update.getPoll().getId());
            long chatId = Long.parseLong(responseStudyOptionsDto.getChatId().trim());
            String userName = responseStudyOptionsDto.getResponseLevelOfStudyDto().getUserName();
            boolean isTimeIntervalClass = studyOptionsService.findByPollId(update.getPoll().getId()).getStartPollTime() +
                    60_000 * levelOfStudyService.findByUserName(userName).getTimeClass() > System.currentTimeMillis();
            if(isTimeIntervalClass){
                teachCommandReceived(chatId, userName);
            }else{
                studyOptionsService.updateStudy(userName, false, null);
                sendMessage(chatId, "Yor time class finish");
                studyOptionsService.updatePoll(userName, null);
            }
        }
    }

    private void timeCommandReceived(long chatId) {
        String answer = "Введите количество минут, которые Вы готовы уделить изучению (целое число)";
        sendMessage(chatId, answer, Options.keyboardTime());
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    private void levelCommandReceived(long chatId, String userName, String level) {
        RequestLevelOfStudyDto requestLevelOfStudyDto = new RequestLevelOfStudyDto();
        requestLevelOfStudyDto.setUserName(userName);
        requestLevelOfStudyDto.setLevelOfStudy(level);
        if (levelOfStudyService.findByUserName(userName) != null) {
            levelOfStudyService.update(requestLevelOfStudyDto);
        } else {
            levelOfStudyService.create(requestLevelOfStudyDto);
        }
        if(studyOptionsService.findByUserName(userName)==null){
            studyOptionsService.create(Options.pollSetNull(chatId, userName, false));
        }else{
            studyOptionsService.updateStudy(userName, false, null);
        }
        String answer = "I am making a program, you can type /teach command to start learning \n " +
                "Training time is 1 minute, if you want to change it, click /time";
        sendMessage(chatId, answer);
    }

    private void startCommandReceived(long chatId, String name, String userName) {
        StringBuilder answer = new StringBuilder("Hi, " + name + ", nice to meet you!");
        if(levelOfStudyService.findByUserName(userName)==null){
            answer.append('\n' + "Indicate your level of knowledge");
            sendMessage(chatId, answer.toString(), Options.keyboardKnowledgeLevel());
        }else{
            sendMessage(chatId, answer.toString());
        }
    }

    private void exitCommandReceived(long chatId, String name) {
        String answer = "Bye, " + name + "!";
        sendMessage(chatId, answer);
    }

    private void teachCommandReceived(long chatId, String userName) {
        ResponseLevelOfStudyDto responseLevelOfStudyDto = levelOfStudyService.findByUserName(userName);
        String level = responseLevelOfStudyDto.getLevelOfStudy();
        Set<ResponseGlossaryDto> glossarySet = glossaryService.findByLevelAll(level);
        List<ResponseGlossaryDto> glossaryDtoList = new ArrayList<>(glossarySet);
        long time = 60_000 * responseLevelOfStudyDto.getTimeClass();
        long start = System.currentTimeMillis();
        long finish;
//        do {
        generatorPoll(chatId, glossaryDtoList, userName);
//            finish = System.currentTimeMillis();
//        } while (finish - start < time);
    }

    private void generatorPoll(Long chatId, List<ResponseGlossaryDto> glossaryDtoList, String userName) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Выберите перевод слова ");
        List<String> options = new ArrayList<>();
        Set<Integer> random = Options.generatorIndex(MAX_QUESTIONS, glossaryDtoList.size());
        int indexQuestion = (int) (Math.random() * MAX_QUESTIONS);
        int i = 0;
        for (Integer item : random) {
            options.add(glossaryDtoList.get(item).getWord().trim());
            if (i == indexQuestion) {
                stringBuilder.append(glossaryDtoList.get(item).getTranslate().trim());
            }
            i++;
        }
        sendMessage(chatId, stringBuilder.toString(), options, indexQuestion, userName);
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendMessage(long chatId, String textToSend, ReplyKeyboardMarkup keyboardMarkup) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        message.setReplyMarkup(keyboardMarkup);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendMessage(long chatId, String question, List<String> options, int correctId, String userName) {
        SendPoll sendPoll = new SendPoll();
        sendPoll.setChatId(String.valueOf(chatId));
        sendPoll.setType("quiz");
        sendPoll.setQuestion(question);
        sendPoll.setIsAnonymous(true);
        sendPoll.setOptions(options);
        sendPoll.setCorrectOptionId(correctId);
        try {
            Poll poll = execute(sendPoll).getPoll();
            studyOptionsService.updatePoll(userName, poll.getId());
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
