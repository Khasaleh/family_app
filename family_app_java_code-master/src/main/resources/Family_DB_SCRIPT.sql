/* ************************************************************************************************************************************************************ */
CREATE DATABASE  IF NOT EXISTS `uploadservice`;
USE `uploadservice`;

/* ********************************************************************************* */
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `roles` VALUES (1,'ROLE_ADMIN'),(2,'ROLE_USER');


/* ********************************************************************************* */
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `enabled` bit(1) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY (`username`),
  UNIQUE KEY (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


INSERT INTO users (`id`,`email`,`enabled`,`password`,`username`)
VALUES
(1,'syed.haider@cybersolution.technology',1,'123456789','luckyman'),
(2,'ali7us@gmail.com',1,'123456789','supermen'),
(3,'salmankhan@gmail.com',1,'123456789','spiderman');


/* ********************************************************************************* */
DROP TABLE IF EXISTS `holiday`;
CREATE TABLE `holiday` (
  `id` int NOT NULL AUTO_INCREMENT,
  `day` varchar(255) NOT NULL,
  `fullDate` date NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `holiday` (`id`,`day`,`fullDate`) 
VALUES (1,'Thursday','20210801'),(2,'Mondy','20210910'), (3,'Friday','20211015');


/* ********************************************************************************* */
DROP TABLE IF EXISTS `Family`;
CREATE TABLE `Family` (
  `famid` int NOT NULL AUTO_INCREMENT,
  `famName` varchar(255) NOT NULL,
  `fam_Iinfo` varchar(500),
  `address` varchar(255),
  `phone_number` varchar(100) ,
  PRIMARY KEY (`famid`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `Family` VALUES 
(1,'Family-1','family member are 4 only','any address', '32106548970'),
(2,'Family-2','family member are 2 only','any address', '32106554115'),
(3,'Family-3','family member are 3 only','any address', '32106544455');


/* ********************************************************************************* */
CREATE TABLE `user_roles` (
  `user_id` int NOT NULL,
  `role_id` int NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY (`role_id`),
  CONSTRAINT FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`),
  CONSTRAINT FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `user_roles` (`user_id`,`role_id`)
VALUES (1,1), (2,1), (3,2);


/* ********************************************************************************* */
DROP TABLE IF EXISTS `main_calendar`;
CREATE TABLE `main_calendar` (
  `id_date` int NOT NULL AUTO_INCREMENT,
  `day` varchar(255) NOT NULL,
  `month` varchar(500) NOT NULL,
  `year` varchar(255) NOT NULL,
  `full_date` date,
  `weekend` varchar(100) NOT NULL,
  `famid` int not null,
  PRIMARY KEY (`id_date`),
  CONSTRAINT FOREIGN KEY (`famid`) REFERENCES `Family` (`famid`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `main_calendar` (`id_date`,`day`,`month`,`year`,`full_date`,`weekend`,`famid`) VALUES 
(1,'Monday', 'January', '2021','2021-09-01','NO',1),
(2,'Friday', 'February', '2021','2021-10-10','NO',2),
(3,'Tuesday', 'March', '2021','2021-08-05','NO',3);


/* ********************************************************************************* */
/* Create image_model table */
DROP TABLE IF EXISTS `image_model`;
CREATE TABLE `image_model` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `date` datetime(6) DEFAULT NULL,
  `extension` varchar(255) DEFAULT NULL,
  `local_path` text,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKtgshfidjqn5923cysspueldxt` (`user_id`),
  CONSTRAINT `FKtgshfidjqn5923cysspueldxt` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `image_model` (`id`,`date`,`extension`,`local_path`,`user_id`)
VALUES (1, '2021-09-01', 'extension', 'path', 1), (2, '2021-08-05', 'extension', 'path', 2);


/* ********************************************************************************* */
DROP TABLE IF EXISTS `Event`;
CREATE TABLE `Event` (
  `eid` int NOT NULL AUTO_INCREMENT,
  `name_of_the_event` varchar(100) DEFAULT NULL,
  `begin_time` time,
  `end_time` time,
  `full_date` date,
  `id_date` int not null,
  `notes` varchar(255),
  PRIMARY KEY (`eid`),
  CONSTRAINT FOREIGN KEY (`id_date`) REFERENCES `main_calendar` (`id_date`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `Event` VALUES 
(1,'icecream', '38:59:59', '18:00:00', '2021-09-01', 1, 'notes-1'),
(2,'freebies', '11:00:00', '18:00:00', '2021-10-01', 1, 'notes-2'),
(3,'sea', '12:00:00', '18:00:00', '2021-08-01', 1, 'notes-3');