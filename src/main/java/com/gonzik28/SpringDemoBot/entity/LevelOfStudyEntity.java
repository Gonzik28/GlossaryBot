package com.gonzik28.SpringDemoBot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = LevelOfStudyEntity.TABLE)
@Getter
@Setter
public class LevelOfStudyEntity {
    public static final String TABLE = "level_of_study";
    @Id
    private String userName;
    private String levelOfStudy;
    @ManyToMany
    @JoinTable(
            name = "glossary_level_of_study",
            joinColumns = @JoinColumn(name = "user_name"),
            inverseJoinColumns = @JoinColumn(name = "word"))
    private Set<GlossaryEntity> glossaryEntitySet = new HashSet<>();

}
