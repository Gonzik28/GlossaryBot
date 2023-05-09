package com.gonzik28.SpringDemoBot.repository;

import com.gonzik28.SpringDemoBot.entity.LevelOfStudyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LevelOfStudyRepository extends JpaRepository<LevelOfStudyEntity, String> {
    Optional<LevelOfStudyEntity> findByUserName(String userName);
    Optional<LevelOfStudyEntity> findByPollId(String pollId);
    void deleteByUserName(String userName);
}
