package com.gonzik28.SpringDemoBot.dto.utils;

import com.gonzik28.SpringDemoBot.dto.RequestLevelOfStudyDto;
import com.gonzik28.SpringDemoBot.dto.RequestStudyOptionsDto;
import com.gonzik28.SpringDemoBot.dto.ResponseLevelOfStudyDto;
import com.gonzik28.SpringDemoBot.dto.ResponseStudyOptionsDto;
import com.gonzik28.SpringDemoBot.entity.LevelOfStudyEntity;
import com.gonzik28.SpringDemoBot.entity.StudyOptionsEntity;

public class StudyOptionsUtils {
    public static ResponseStudyOptionsDto optionsEntityToDto(StudyOptionsEntity studyOptions) {
        ResponseStudyOptionsDto responseStudyOptionsDto = new ResponseStudyOptionsDto();
        responseStudyOptionsDto.setId(studyOptions.getId());
        LevelOfStudyEntity levelOfStudyEntity = studyOptions.getLevelOfStudy();
        ResponseLevelOfStudyDto levelOfStudyDto = LevelOfStudyUtils.levelOfStudyEntityToDto(levelOfStudyEntity);
        responseStudyOptionsDto.setResponseLevelOfStudyDto(levelOfStudyDto);
        responseStudyOptionsDto.setStudy(studyOptions.isStudy());
        if(studyOptions.getChatId()!=null){
            responseStudyOptionsDto.setChatId(studyOptions.getChatId());
        }
        if(studyOptions.getPollId()!=null){
            responseStudyOptionsDto.setPollId(studyOptions.getPollId());
        }
        return responseStudyOptionsDto;
    }

    public static StudyOptionsEntity levelOfStudyDtoToEntity(RequestStudyOptionsDto requestStudyOptionsDto,
                                                             LevelOfStudyEntity levelOfStudy) {
        StudyOptionsEntity studyOptionsEntity = new StudyOptionsEntity();
        studyOptionsEntity.setId(requestStudyOptionsDto.getId());
        studyOptionsEntity.setLevelOfStudy(levelOfStudy);
        studyOptionsEntity.setStudy(requestStudyOptionsDto.isStudy());
        if(requestStudyOptionsDto.getChatId()!=null){
            studyOptionsEntity.setChatId(requestStudyOptionsDto.getChatId());
        }
        if(requestStudyOptionsDto.getPollId()!=null){
            studyOptionsEntity.setPollId(requestStudyOptionsDto.getPollId());
        }
        return studyOptionsEntity;
    }
}
