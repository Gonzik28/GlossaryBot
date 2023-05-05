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

    public LevelOfStudyService(GlossaryRepository glossaryRepository, LevelOfStudyRepository levelOfStudyRepository) {
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

    public ResponseLevelOfStudyDto create(RequestLevelOfStudyDto levelOfStudyDto) {
        if (levelOfStudyRepository.findByUserName(levelOfStudyDto.getUserName()).isPresent()) {
            return update(levelOfStudyDto);
        } else {
            Set<GlossaryEntity> glossaryEntitySet = glossaryRepository.findAllByLevel(levelOfStudyDto.getLevelOfStudy());
            LevelOfStudyEntity levelOfStudyEntity = LevelOfStudyUtils.levelOfStudyDtoToEntity(levelOfStudyDto);
            levelOfStudyEntity.setGlossaryEntitySet(glossaryEntitySet);
            levelOfStudyEntity.setTimeClass(1);
            levelOfStudyEntity = levelOfStudyRepository.save(levelOfStudyEntity);
            return LevelOfStudyUtils.levelOfStudyEntityToDto(levelOfStudyEntity);
        }
    }

    public ResponseLevelOfStudyDto update(RequestLevelOfStudyDto levelOfStudyDto) {
        String level;
        int time;
        Set<GlossaryEntity> glossaryEntitySet;
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
            if (levelOfStudyDto.getLevelOfStudy() == null) {
                level = levelOfStudyEntity.getLevelOfStudy();
                glossaryEntitySet = levelOfStudyEntity.getGlossaryEntitySet();
            } else {
                levelOfStudyEntity.setGlossaryEntitySet(null);
                levelOfStudyEntity = levelOfStudyRepository.save(levelOfStudyEntity);

                level = levelOfStudyDto.getLevelOfStudy();
                glossaryEntitySet = glossaryRepository.findAllByLevel(level);
            }
            levelOfStudyEntity.setLevelOfStudy(level);
            levelOfStudyEntity.setGlossaryEntitySet(glossaryEntitySet);

            levelOfStudyEntity = levelOfStudyRepository.save(levelOfStudyEntity);
            return LevelOfStudyUtils.levelOfStudyEntityToDto(levelOfStudyEntity);
        }
    }


    public void delete(String userName) {
        levelOfStudyRepository.deleteByUserName(userName);
    }

}
