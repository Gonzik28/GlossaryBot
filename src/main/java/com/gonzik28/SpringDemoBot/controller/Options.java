package com.gonzik28.SpringDemoBot.controller;

import com.gonzik28.SpringDemoBot.dto.RequestLevelOfStudyDto;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class Options {

    static ReplyKeyboardMarkup keyboardKnowledgeLevel() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardButtons = new KeyboardRow();
        keyboardButtons.add("A0");
        keyboardButtons.add("A1");
        keyboardButtons.add("A2");
        keyboardRows.add(keyboardButtons);
        keyboardButtons = new KeyboardRow();
        keyboardButtons.add("B1");
        keyboardButtons.add("B2");
        keyboardRows.add(keyboardButtons);
        keyboardButtons = new KeyboardRow();
        keyboardButtons.add("C1");
        keyboardButtons.add("C2");
        keyboardRows.add(keyboardButtons);
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup keyboardTime() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(new KeyboardButton("Отправить"));
        keyboard.add(keyboardRow);
        replyKeyboardMarkup.setKeyboard(keyboard);
        replyKeyboardMarkup.setResizeKeyboard(true);
        return replyKeyboardMarkup;
    }

    static RequestLevelOfStudyDto updateTime(String userName, Integer number){
        RequestLevelOfStudyDto requestLevelOfStudyDto = new RequestLevelOfStudyDto();
        requestLevelOfStudyDto.setUserName(userName);
        requestLevelOfStudyDto.setTimeClass(number);
        return requestLevelOfStudyDto;
    }
    static Set<Integer> generatorIndex(int maxValue, int sizeSet) {
        Set<Integer> indexes = new HashSet<>();
        while (indexes.size() < maxValue) {
            int ind = ThreadLocalRandom.current().nextInt(0, sizeSet);
            indexes.add(ind);
        }
        return indexes;
    }

    static SetMyCommands generatorMenu() {
        List<BotCommand> commands = new ArrayList<>();
        commands.add(new BotCommand("/start", "Начало работы и выбор уровня"));
        commands.add(new BotCommand("/time", "Длительность одного урока"));
        commands.add(new BotCommand("/teach", "Начало обучения"));
        commands.add(new BotCommand("/exit", "Завершение обучения"));
        commands.add(new BotCommand("/help", "Подсказка о командах"));
        return new SetMyCommands(commands, new BotCommandScopeDefault(), null);
    }
}
