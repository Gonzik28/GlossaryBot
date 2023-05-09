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
    @EmbeddedId
    private GlossaryId id;
    private String level;
}
