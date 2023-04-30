package com.gonzik28.SpringDemoBot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = GlossaryEntity.TABLE)
@Getter
@Setter
public class GlossaryEntity {
    public static final String TABLE = "glossary";
    @Id
    private String word;
    private String translate;
    private String level;
    @ManyToMany(mappedBy = "glossaryEntitySet")
    private Set<LevelOfStudyEntity> levelOfStudyEntities = new HashSet<>();
}
