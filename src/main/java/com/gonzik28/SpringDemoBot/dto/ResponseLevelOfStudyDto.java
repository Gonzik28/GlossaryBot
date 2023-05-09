package com.gonzik28.SpringDemoBot.dto;

import com.gonzik28.SpringDemoBot.entity.GlossaryEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class ResponseLevelOfStudyDto {
    private String userName;
    private String levelOfStudy;
    private int timeClass;
    private boolean study;
    private String chatId;
    private String pollId;
    private Long startPollTime;
}
