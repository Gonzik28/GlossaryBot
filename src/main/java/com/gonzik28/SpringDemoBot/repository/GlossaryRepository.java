package com.gonzik28.SpringDemoBot.repository;

import com.gonzik28.SpringDemoBot.entity.GlossaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface GlossaryRepository extends JpaRepository<GlossaryEntity, String> {
    Set<GlossaryEntity> findAllByLevel(String level);
    Optional<GlossaryEntity> findByWord(String word);
}
