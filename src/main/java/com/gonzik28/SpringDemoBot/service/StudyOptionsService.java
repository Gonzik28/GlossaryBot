package com.gonzik28.SpringDemoBot.service;

import com.gonzik28.SpringDemoBot.dto.RequestStudyOptionsDto;
import com.gonzik28.SpringDemoBot.dto.ResponseStudyOptionsDto;
import com.gonzik28.SpringDemoBot.dto.utils.LevelOfStudyUtils;
import com.gonzik28.SpringDemoBot.dto.utils.StudyOptionsUtils;


import com.gonzik28.SpringDemoBot.entity.LevelOfStudyEntity;
import com.gonzik28.SpringDemoBot.entity.StudyOptionsEntity;
import com.gonzik28.SpringDemoBot.repository.LevelOfStudyRepository;
import com.gonzik28.SpringDemoBot.repository.StudyOptionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.polls.Poll;

import java.util.NoSuchElementException;

@Service
@Transactional
public class StudyOptionsService {

    @Autowired
    private StudyOptionsRepository studyOptionsRepository;
    @Autowired
    private LevelOfStudyRepository levelOfStudyRepository;

    public ResponseStudyOptionsDto findByUserName(String userName) {
        if (levelOfStudyRepository.findByUserName(userName).isPresent()) {
            LevelOfStudyEntity levelOfStudyEntity = levelOfStudyRepository.findByUserName(userName).get();
            if (studyOptionsRepository.findByLevelOfStudy(levelOfStudyEntity).isPresent()) {
                StudyOptionsEntity studyOptionsEntity = studyOptionsRepository.findByLevelOfStudy(levelOfStudyEntity).get();
                return StudyOptionsUtils.optionsEntityToDto(studyOptionsEntity);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public ResponseStudyOptionsDto findByPollId(String pollId) {
        if (studyOptionsRepository.findByPollId(pollId).isPresent()) {
            StudyOptionsEntity studyOptionsEntity = studyOptionsRepository.findByPollId(pollId).get();
            return StudyOptionsUtils.optionsEntityToDto(studyOptionsEntity);
        } else {
            return null;
        }
    }


    public ResponseStudyOptionsDto create(RequestStudyOptionsDto requestStudyOptionsDto) {
        String userName = requestStudyOptionsDto.getUserName();
        LevelOfStudyEntity levelOfStudyEntity = levelOfStudyRepository.findByUserName(userName).get();
        StudyOptionsEntity studyOptionsEntity =
                StudyOptionsUtils.levelOfStudyDtoToEntity(requestStudyOptionsDto, levelOfStudyEntity);
        studyOptionsEntity = studyOptionsRepository.save(studyOptionsEntity);
        return StudyOptionsUtils.optionsEntityToDto(studyOptionsEntity);
    }

    public void updateTime(String userName, boolean isStudy) {
        LevelOfStudyEntity levelOfStudyEntity = levelOfStudyRepository.findByUserName(userName).get();
        if (!studyOptionsRepository.findByLevelOfStudy(levelOfStudyEntity).isPresent()) {
            throw new NoSuchElementException("Вы еще не зарегистрированны");
        } else {
            StudyOptionsEntity studyOptionsEntity =
                    studyOptionsRepository.findByLevelOfStudy(levelOfStudyEntity).get();
            studyOptionsEntity.setStudy(isStudy);
            studyOptionsRepository.save(studyOptionsEntity);
        }
    }

    public void updatePoll(String userName, String pollId) {
        LevelOfStudyEntity levelOfStudyEntity = levelOfStudyRepository.findByUserName(userName).get();
        if (!studyOptionsRepository.findByLevelOfStudy(levelOfStudyEntity).isPresent()) {
            throw new NoSuchElementException("Вы еще не зарегистрированны");
        } else {
            StudyOptionsEntity studyOptionsEntity =
                    studyOptionsRepository.findByLevelOfStudy(levelOfStudyEntity).get();
            studyOptionsEntity.setPollId(pollId);
            studyOptionsRepository.save(studyOptionsEntity);
        }
    }

    public void updateStudy(String userName, boolean isStudy) {
        LevelOfStudyEntity levelOfStudyEntity = levelOfStudyRepository.findByUserName(userName).get();
        if (!studyOptionsRepository.findByLevelOfStudy(levelOfStudyEntity).isPresent()) {
            throw new NoSuchElementException("Вы еще не зарегистрированны");
        } else {
            StudyOptionsEntity studyOptionsEntity =
                    studyOptionsRepository.findByLevelOfStudy(levelOfStudyEntity).get();
            studyOptionsEntity.setStudy(isStudy);
            studyOptionsRepository.save(studyOptionsEntity);
        }
    }
}
