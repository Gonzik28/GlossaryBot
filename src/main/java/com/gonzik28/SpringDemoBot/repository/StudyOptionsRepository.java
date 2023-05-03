package com.gonzik28.SpringDemoBot.repository;

import com.gonzik28.SpringDemoBot.entity.LevelOfStudyEntity;
import com.gonzik28.SpringDemoBot.entity.StudyOptionsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudyOptionsRepository extends JpaRepository<StudyOptionsEntity, String> {
    Optional<StudyOptionsEntity> findByLevelOfStudy(LevelOfStudyEntity levelOfStudyEntity);
    Optional<StudyOptionsEntity> findByPollId(String pollId);
}
