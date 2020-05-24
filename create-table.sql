CREATE TABLE `general` (
    `index` INTEGER,
    `title` VARCHAR(255) NOT NULL,
	`status` INTEGER NOT NULL,
	`importancy` INTEGER NOT NULL
);
CREATE TABLE `planned` (
    `title` VARCHAR(255) NOT NULL,
	`status` INTEGER NOT NULL,
	`date` DATE NOT NULL
);
CREATE TABLE `objective` (
    `index` INTEGER NOT NULL,
    `title` VARCHAR(255) NOT NULL,
	`date` DATE NOT NULL,
	PRIMARY KEY(`index`)
);
CREATE TABLE `toDoList` (
    `title` VARCHAR(255) NOT NULL
);