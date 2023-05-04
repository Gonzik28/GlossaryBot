package com.gonzik28.SpringDemoBot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.objects.polls.Poll;

@Entity
@Table(name = StudyOptionsEntity.TABLE)
@Getter
@Setter
public class StudyOptionsEntity {
    public static final String TABLE = "study_options";
    @Id
    private String id;
    @OneToOne
    @JoinColumn(name = "user_name")
    private LevelOfStudyEntity levelOfStudy;
    private boolean study;
    private String chatId;
    @Column(name = "poll_id")
    private String pollId;
    private Long startPollTime;
}
