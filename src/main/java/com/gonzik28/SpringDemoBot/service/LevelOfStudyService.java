package com.gonzik28.SpringDemoBot.service;

import com.gonzik28.SpringDemoBot.dto.RequestLevelOfStudyDto;
import com.gonzik28.SpringDemoBot.dto.ResponseLevelOfStudyDto;
import com.gonzik28.SpringDemoBot.dto.utils.LevelOfStudyUtils;
import com.gonzik28.SpringDemoBot.entity.GlossaryEntity;
import com.gonzik28.SpringDemoBot.entity.LevelOfStudyEntity;
import com.gonzik28.SpringDemoBot.repository.GlossaryRepository;
import com.gonzik28.SpringDemoBot.repository.LevelOfStudyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Set;

@Service
@Transactional
public class LevelOfStudyService {
    private final LevelOfStudyRepository levelOfStudyRepository;
    private final GlossaryRepository glossaryRepository;

    public LevelOfStudyService(GlossaryRepository glossaryRepository,
                               LevelOfStudyRepository levelOfStudyRepository) {
        this.glossaryRepository = glossaryRepository;
        this.levelOfStudyRepository = levelOfStudyRepository;
    }

    public ResponseLevelOfStudyDto findByUserName(String userName) {
        if (levelOfStudyRepository.findByUserName(userName).isPresent()) {
            LevelOfStudyEntity levelOfStudyEntity = levelOfStudyRepository.findByUserName(userName).get();
            return LevelOfStudyUtils.levelOfStudyEntityToDto(levelOfStudyEntity);
        } else {
            return null;
        }
    }

    public ResponseLevelOfStudyDto findByPollId(String id) {
        if (levelOfStudyRepository.findByPollId(id).isPresent()) {
            LevelOfStudyEntity levelOfStudyEntity = levelOfStudyRepository.findByPollId(id).get();
            return LevelOfStudyUtils.levelOfStudyEntityToDto(levelOfStudyEntity);
        } else {
            return null;
        }
    }

    public ResponseLevelOfStudyDto create(RequestLevelOfStudyDto levelOfStudyDto) {
        if (levelOfStudyRepository.findByUserName(levelOfStudyDto.getUserName()).isPresent()) {
            return update(levelOfStudyDto);
        } else {
            if(levelOfStudyDto.getTimeClass()==null){
                levelOfStudyDto.setTimeClass(1);
            }
            LevelOfStudyEntity levelOfStudyEntity = LevelOfStudyUtils.levelOfStudyDtoToEntity(levelOfStudyDto);
            levelOfStudyEntity = levelOfStudyRepository.save(levelOfStudyEntity);
            return LevelOfStudyUtils.levelOfStudyEntityToDto(levelOfStudyEntity);
        }
    }

    public ResponseLevelOfStudyDto update(RequestLevelOfStudyDto levelOfStudyDto) {
        String level;
        int time;
        if (!levelOfStudyRepository.findByUserName(levelOfStudyDto.getUserName()).isPresent()) {
            throw new NoSuchElementException("Вы еще не зарегистрированны");
        } else {
            LevelOfStudyEntity levelOfStudyEntity = levelOfStudyRepository
                    .findByUserName(levelOfStudyDto.getUserName()).get();
            if (levelOfStudyDto.getTimeClass() == null) {
                time = levelOfStudyEntity.getTimeClass();
            } else {
                time = levelOfStudyDto.getTimeClass();
            }
            levelOfStudyEntity.setTimeClass(time);
            if(levelOfStudyDto.getLevelOfStudy() == null){
                level = levelOfStudyEntity.getLevelOfStudy();
            }else{
                level = levelOfStudyDto.getLevelOfStudy();
            }
            levelOfStudyEntity.setLevelOfStudy(level);
            levelOfStudyEntity = levelOfStudyRepository.save(levelOfStudyEntity);
            return LevelOfStudyUtils.levelOfStudyEntityToDto(levelOfStudyEntity);
        }
    }

    public void updatePoll(String userName, String id) {
        if (!levelOfStudyRepository.findByUserName(userName).isPresent()) {
            throw new NoSuchElementException("Вы еще не зарегистрированны");
        } else {
            LevelOfStudyEntity levelOfStudyEntity = levelOfStudyRepository
                    .findByUserName(userName).get();
            levelOfStudyEntity.setPollId(id);
            levelOfStudyRepository.save(levelOfStudyEntity);
        }
    }


    public void delete(String userName) {
        levelOfStudyRepository.deleteByUserName(userName);
    }


    public void updateStudy(String userName, boolean isStudy, Long currentTimeMillis) {
        if (!levelOfStudyRepository.findByUserName(userName).isPresent()) {
            throw new NoSuchElementException("Вы еще не зарегистрированны");
        } else {
            LevelOfStudyEntity levelOfStudyEntity = levelOfStudyRepository
                    .findByUserName(userName).get();
            levelOfStudyEntity.setStudy(isStudy);
            levelOfStudyEntity.setStartPollTime(currentTimeMillis);
            levelOfStudyRepository.save(levelOfStudyEntity);
        }
    }

}
