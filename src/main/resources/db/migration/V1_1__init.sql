CREATE TABLE IF NOT EXISTS glossary
(
    word char(200) NOT NULL,
    translate char(200) NOT NULL,
    level char(2) NOT NULL,
    CONSTRAINT pk_glossary_id PRIMARY KEY (word, translate)
);

CREATE TABLE IF NOT EXISTS level_of_study
(
    user_name char(100) NOT NULL,
    level_of_study char(2) NOT NULL,
    time_class INT DEFAULT 1,
    CONSTRAINT pk_level_of_study_id PRIMARY KEY (user_name)
);

CREATE TABLE glossary_level_of_study (
    user_name VARCHAR(255) NOT NULL,
    glossary_key_word VARCHAR(255) NOT NULL,
    glossary_key_translate VARCHAR(255) NOT NULL,
    PRIMARY KEY (user_name, glossary_key_word, glossary_key_translate),
    CONSTRAINT fk_glossary_level_of_study_level_of_study FOREIGN KEY (user_name)
        REFERENCES level_of_study (user_name),
    CONSTRAINT fk_glossary_level_of_study_glossary FOREIGN KEY (glossary_key_word, glossary_key_translate)
        REFERENCES glossary (word, translate)
);

CREATE TABLE IF NOT EXISTS study_options
(
    id char(200) NOT NULL,
    user_name char(100) NOT NULL,
    study BOOLEAN DEFAULT false,
    chat_id char(200),
    poll_id char(300),
    CONSTRAINT fk_level_of_study FOREIGN KEY (user_name) REFERENCES level_of_study(user_name),
    CONSTRAINT pk_study_options_id PRIMARY KEY (id)
)