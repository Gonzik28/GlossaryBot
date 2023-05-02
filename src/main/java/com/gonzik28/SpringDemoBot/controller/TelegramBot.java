package com.gonzik28.SpringDemoBot.controller;

import com.gonzik28.SpringDemoBot.config.BotConfig;

import com.gonzik28.SpringDemoBot.dto.RequestLevelOfStudyDto;
import com.gonzik28.SpringDemoBot.dto.ResponseGlossaryDto;
import com.gonzik28.SpringDemoBot.dto.ResponseLevelOfStudyDto;
import com.gonzik28.SpringDemoBot.service.GlossaryService;
import com.gonzik28.SpringDemoBot.service.LevelOfStudyService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    final BotConfig config;
    Boolean a = false;
    private final LevelOfStudyService levelOfStudyService;
    private final GlossaryService glossaryService;
    private final int MAX_QUESTIONS = 3;
    private static final String HELP_TEXT = "This bot is creating to English glossary.\n"
            + "Type /start too see a welcome message \n"
            + "Type /time change time quiz \n"
            + "Type /teach too see a quiz message \n"
            + "Type /exit too see a exit message";

    public TelegramBot(BotConfig config, LevelOfStudyService levelOfStudyService,
                       GlossaryService glossaryService) {
        this.config = config;
        this.levelOfStudyService = levelOfStudyService;
        this.glossaryService = glossaryService;
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
                if (levelOfStudyService.findByUserName(userName).getStudy()) {
                    Integer num = Integer.parseInt(messageText);
                    if (num > 0) {
                        levelOfStudyService.update(Options.updateTimeFalse(userName, num));
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
                    startCommandReceived(chatId, firstName);
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
                    levelOfStudyService.updateTime(userName, true);
                    timeCommandReceived(chatId);
                    break;
                case "/teach":
                    teachCommandReceived(chatId, userName);
                    break;
                case "/exit":
                    exitCommandReceived(chatId, firstName);
                    break;
                case "/help":
                    sendMessage(chatId, HELP_TEXT);
                    break;
                default:
                    sendMessage(chatId, "Sorry, command was not recognized");
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
        requestLevelOfStudyDto.setStudy(false);
        if(levelOfStudyService.findByUserName(userName)!=null){
            levelOfStudyService.update(requestLevelOfStudyDto);
        }else {
            levelOfStudyService.create(requestLevelOfStudyDto);
        }
        String answer = "I am making a program, you can type /teach command to start learning \n " +
                "Training time is 1 minute, if you want to change it, click /time";
        sendMessage(chatId, answer);
    }

    private void startCommandReceived(long chatId, String name) {
        String answer = "Hi, " + name + ", nice to meet you!" + '\n' + "Indicate your level of knowledge";
        sendMessage(chatId, answer, Options.keyboardKnowledgeLevel());
    }

    private void exitCommandReceived(long chatId, String name) {
        String answer = "Bye, " + name + "!";
        sendMessage(chatId, answer);
    }


    private void teachCommandReceived(long chatId, String userName) {
        ResponseLevelOfStudyDto responseLevelOfStudyDto = levelOfStudyService.findByUserName(userName);
        long time = 60_000 * responseLevelOfStudyDto.getTimeClass();
        long start = System.currentTimeMillis();
        long finish;
        do {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Выберите перевод слова ");
            String level = responseLevelOfStudyDto.getLevelOfStudy();
            Set<ResponseGlossaryDto> glossarySet = glossaryService.findByLevelAll(level);
            List<ResponseGlossaryDto> glossaryDtoList = new ArrayList<>(glossarySet);
            List<String> options = new ArrayList<>();
            Set<Integer> random = Options.generatorIndex(MAX_QUESTIONS, glossarySet.size());
            int indexQuestion = (int) (Math.random() * MAX_QUESTIONS);
            int i = 0;
            for (Integer item : random) {
                ResponseGlossaryDto responseGlossaryDto = glossaryDtoList.get(item);
                options.add(responseGlossaryDto.getWord().trim());
                if (i == indexQuestion) {
                    stringBuilder.append(responseGlossaryDto.getTranslate().trim());
                }
                i++;
            }
            try {
                sendMessage(chatId, stringBuilder.toString(), options, indexQuestion);
                Thread.sleep(5_000);
                finish = System.currentTimeMillis();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } while (finish - start < time);

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

    private void sendMessage(long chatId, String question, List<String> options, int correctId) {
        SendPoll sendPoll = new SendPoll();
        sendPoll.setChatId(String.valueOf(chatId));
        sendPoll.setType("quiz");
        sendPoll.setQuestion(question);
        sendPoll.setIsAnonymous(true);
        sendPoll.setOptions(options);
        sendPoll.setCorrectOptionId(correctId);
        try {
            execute(sendPoll);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
