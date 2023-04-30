DROP TABLE glossary_level_of_study;
DROP TABLE level_of_study;
DROP TABLE glossary;

CREATE TABLE IF NOT EXISTS glossary
(
    word char(200) NOT NULL,
    translate char(200) NOT NULL,
    level char(2) NOT NULL,
    CONSTRAINT pk_glossary_id PRIMARY KEY (word)
    );

CREATE TABLE IF NOT EXISTS level_of_study
(
    user_name char(100) NOT NULL,
    level_of_study char(2) NOT NULL,
    CONSTRAINT pk_level_of_study_id PRIMARY KEY (user_name)
    );

CREATE TABLE IF NOT EXISTS glossary_level_of_study
(
    word char(1000) NOT NULL,
    user_name char(100) NOT NULL,
    CONSTRAINT fk_glossary FOREIGN KEY (word) REFERENCES glossary(word),
    CONSTRAINT fk_level_of_study FOREIGN KEY (user_name) REFERENCES level_of_study(user_name),
    CONSTRAINT pk_glossary_level_of_study PRIMARY KEY (word, user_name)
);