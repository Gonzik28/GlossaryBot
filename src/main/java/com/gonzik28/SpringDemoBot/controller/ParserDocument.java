package com.gonzik28.SpringDemoBot.controller;

import com.gonzik28.SpringDemoBot.dto.RequestGlossaryDto;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ParserDocument {

    public static List<RequestGlossaryDto> parserCSV(String path){
        ArrayList<RequestGlossaryDto> glossaryDtos = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(path));
            for (String line : lines) {
                String[] fragments = line.split(",");
                if (correctFragment(fragments)){
                    RequestGlossaryDto requestGlossaryDto = new RequestGlossaryDto();
                    requestGlossaryDto.setWord(fragments[0]);
                    requestGlossaryDto.setTranslate(fragments[1]);
                    requestGlossaryDto.setLevel(fragments[2]);
                    glossaryDtos.add(requestGlossaryDto);
                }else{
                    System.out.println("Fail");
                    continue;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return glossaryDtos;
    }

    public static boolean correctFragment(String[] fragments){
        boolean correctArrSize = fragments.length == 3;
        boolean correctEnglishAlphabet = fragments[0].matches("^[a-zA-Z0-9\\s-]+$");
        boolean correctRussianAlphabet = fragments[1].matches("^[а-яА-ЯёЁ0-9\\s-]+$");
        boolean correctLevel = fragments[2].matches("^[A-C][0-2]$");
        if(correctArrSize && correctEnglishAlphabet && correctRussianAlphabet && correctLevel){
            return true;
        }else {
            return false;
        }
    }
}
