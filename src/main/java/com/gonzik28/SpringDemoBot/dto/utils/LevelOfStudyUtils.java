package com.gonzik28.SpringDemoBot.dto.utils;

import com.gonzik28.SpringDemoBot.dto.RequestLevelOfStudyDto;
import com.gonzik28.SpringDemoBot.dto.ResponseLevelOfStudyDto;
import com.gonzik28.SpringDemoBot.entity.LevelOfStudyEntity;


public class LevelOfStudyUtils {
    public static ResponseLevelOfStudyDto levelOfStudyEntityToDto(LevelOfStudyEntity studyEntity) {
        ResponseLevelOfStudyDto responseLevelOfStudyDto = new ResponseLevelOfStudyDto();
        responseLevelOfStudyDto.setLevelOfStudy(studyEntity.getLevelOfStudy());
        responseLevelOfStudyDto.setUserName(studyEntity.getUserName());
        responseLevelOfStudyDto.setGlossaries(studyEntity.getGlossaryEntitySet());
        responseLevelOfStudyDto.setTimeClass(studyEntity.getTimeClass());
        return responseLevelOfStudyDto;
    }

    public static LevelOfStudyEntity levelOfStudyDtoToEntity(RequestLevelOfStudyDto requestLevelOfStudyDto) {
        LevelOfStudyEntity studyEntity = new LevelOfStudyEntity();
        studyEntity.setLevelOfStudy(requestLevelOfStudyDto.getLevelOfStudy());
        studyEntity.setUserName(requestLevelOfStudyDto.getUserName());
        return studyEntity;
    }
}