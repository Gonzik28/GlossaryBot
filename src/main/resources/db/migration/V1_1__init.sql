CREATE TABLE IF NOT EXISTS glossary
(
    id char NOT NULL,
    word char NOT NULL,
    translate char NOT NULL,
    CONSTRAINT pk_glossary_id PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS level_of_study
(
    id char NOT NULL,
    nick_study char NOT NULL,
    level_of_study char NOT NULL,
    CONSTRAINT pk_level_of_study_id PRIMARY KEY (id)
    );