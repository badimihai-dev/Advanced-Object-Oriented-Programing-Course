CREATE TABLE `general` (
    `index` int(16) auto_increment,
    `title` VARCHAR(255) NOT NULL,
    `status` INTEGER NOT NULL,
    `importancy` INTEGER NOT NULL,
    PRIMARY KEY(`index`)
);

CREATE TABLE `planned` (
    `index` int(16) auto_increment,
    `title` VARCHAR(255) NOT NULL,
    `status` INTEGER NOT NULL,
    `date` VARCHAR(255) NOT NULL,
    PRIMARY KEY(`index`)
);

CREATE TABLE `objective` (
    `index` int(16) auto_increment,
    `title` VARCHAR(255) NOT NULL,
    `status` INTEGER NOT NULL,
    `date` VARCHAR(255) NOT NULL,
    PRIMARY KEY(`index`)
);

CREATE TABLE `o-tasks` (
    `index` INTEGER NOT NULL,
    `title` VARCHAR(255) NOT NULL,
    `status` INTEGER NOT NULL,
    `taskid` int(16) auto_increment,
    PRIMARY KEY(`taskid`),
    FOREIGN KEY(`index`) REFERENCES `objective`(`index`) ON DELETE CASCADE
);

CREATE TABLE `lists` (
    `index` int(16) auto_increment,
    `title` VARCHAR(255) NOT NULL,
    PRIMARY KEY(`index`)
);

CREATE TABLE `l-tasks` (
    `index` INTEGER NOT NULL,
    `title` VARCHAR(255) NOT NULL,
    `status` INTEGER NOT NULL,
    `importancy` INTEGER NOT NULL,
    `taskid` int(16) auto_increment,
    PRIMARY KEY(`taskid`),
    FOREIGN KEY(`index`) REFERENCES `lists`(`index`) ON DELETE CASCADE
);

ALTER TABLE `general` AUTO_INCREMENT=1;
ALTER TABLE `planned` AUTO_INCREMENT=1;
ALTER TABLE `objective` AUTO_INCREMENT=1;
ALTER TABLE `lists` AUTO_INCREMENT=1;
ALTER TABLE `l-tasks` AUTO_INCREMENT=1;
ALTER TABLE `o-tasks` AUTO_INCREMENT=1;