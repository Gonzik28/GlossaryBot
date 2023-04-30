package com.gonzik28.SpringDemoBot.dto;

import com.gonzik28.SpringDemoBot.entity.LevelOfStudyEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class ResponseGlossaryDto {
    private String word;
    private String translate;
    private String level;
    private Set<LevelOfStudyEntity> levelsOfStudy;
}

