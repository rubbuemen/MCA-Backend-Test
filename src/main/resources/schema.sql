create table if not exists VIDEOGAME
(
    ID    int          not null AUTO_INCREMENT,
    TITLE varchar(100) not null,
    PRIMARY KEY (ID)
);
create table if not exists PROMOTION
(
    ID           int not null AUTO_INCREMENT,
    VALID_FROM   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRICE        numeric(5, 2),
    VIDEOGAME_ID int not null,
    PRIMARY KEY (ID),
    CONSTRAINT fk_VIDEOGAME_ID_PROMOTION
        FOREIGN KEY (VIDEOGAME_ID)
            REFERENCES VIDEOGAME (ID)
);


create table if not exists STOCK
(
    ID      INT        not null AUTO_INCREMENT,
    AVAILABILITY boolean,
    LAST_UPDATED TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    VIDEOGAME_ID int        not null,
    PRIMARY KEY (ID),
    CONSTRAINT fk_VIDEOGAME_ID_STOCK
        FOREIGN KEY (VIDEOGAME_ID)
            REFERENCES VIDEOGAME (ID)
);





