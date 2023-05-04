package com.gonzik28.SpringDemoBot.dto;

import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.objects.polls.Poll;

@Getter
@Setter
public class RequestStudyOptionsDto {
    private String id;
    private String userName;
    private String chatId;
    private String pollId;
    private Long startPollTime;
    private boolean study;
}
