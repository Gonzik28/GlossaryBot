package com.gonzik28.SpringDemoBot.dto;

import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.objects.polls.Poll;

@Getter
@Setter
public class ResponseStudyOptionsDto {
    private String id;
    private ResponseLevelOfStudyDto responseLevelOfStudyDto;
    private boolean study;
    private String chatId;
    private String pollId;
}
