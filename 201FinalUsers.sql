USE 201Final;

DROP TABLE IF EXISTS UserInfo;
DROP TABLE IF EXISTS DocumentManager;

CREATE TABLE UserInfo(
    username VARCHAR(12) PRIMARY KEY,
    password  VARCHAR(20) NOT NULL
);

CREATE TABLE DocumentManager(
	documentID INT(11) PRIMARY KEY AUTO_INCREMENT,
    docName VARCHAR(20) NOT NULL,
    docContent MEDIUMBLOB
);