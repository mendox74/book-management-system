DROP TABLE IF EXISTS author;
DROP TABLE IF EXISTS books;

CREATE TABLE `author` (
 `id` int(11) NOT NULL AUTO_INCREMENT,
 `name` varchar(255) NOT NULL,
 PRIMARY KEY (`id`)
) ENGINE=INNODB;

CREATE TABLE `books` (
 `id` int(11) NOT NULL AUTO_INCREMENT,
 `title` varchar(255) NOT NULL,
 `content` longtext DEFAULT NULL,
 `author_id` int(11) NOT NULL,
 PRIMARY KEY (`id`),
 FOREIGN KEY (`author_id`)
 REFERENCES author (`id`)
) ENGINE=INNODB;
