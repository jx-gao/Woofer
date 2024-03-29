CREATE TABLE USERS (
    USERNAME VARCHAR(40) NOT NULL,
    HASHEDPASS CHAR(64) NOT NULL,
    PRIMARY KEY (USERNAME)
);

CREATE TABLE STATUS (
    STATUS_ID INT(10) NOT NULL AUTO_INCREMENT,
    USERNAME VARCHAR(40) NOT NULL,
    DATE DATETIME NOT NULL,
    CONTENT TEXT NOT NULL,
    PRIMARY KEY (STATUS_ID),
    FOREIGN KEY (USERNAME) REFERENCES USERS(USERNAME)
);

CREATE TABLE FRIENDS (
    USERNAME VARCHAR(40) NOT NULL,
    FRIEND VARCHAR(40) NOT NULL,
    PRIMARY KEY (USERNAME,FRIEND),
    FOREIGN KEY (USERNAME) REFERENCES USERS(USERNAME),
    FOREIGN KEY (FRIEND) REFERENCES USERS(USERNAME)
);