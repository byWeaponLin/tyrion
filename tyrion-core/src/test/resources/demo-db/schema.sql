create database if not exists demo;

use demo;

CREATE TABLE if not exists `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `gender` varchar(20) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

INSERT INTO demo.user (id, name, gender, age) VALUES (1, 'weapon', 'male', 11);
INSERT INTO demo.user (id, name, gender, age) VALUES (2, 'weaponbaba2', 'm', 24);
INSERT INTO demo.user (id, name, gender, age) VALUES (3, 'lin', 'male', 20);
INSERT INTO demo.user (id, name, gender, age) VALUES (4, 'lin', 'male', 20);
INSERT INTO demo.user (id, name, gender, age) VALUES (5, 'lin', 'male', 20);
INSERT INTO demo.user (id, name, gender, age) VALUES (6, 'lin', 'male', 20);
INSERT INTO demo.user (id, name, gender, age) VALUES (7, 'lin', 'male', 21);
INSERT INTO demo.user (id, name, gender, age) VALUES (8, 'lin', 'male', 20);
INSERT INTO demo.user (id, name, gender, age) VALUES (9, 'lin', 'male', 21);
INSERT INTO demo.user (id, name, gender, age) VALUES (10, 'lin', 'male', 20);
INSERT INTO demo.user (id, name, gender, age) VALUES (11, 'lin', 'male', 20);
INSERT INTO demo.user (id, name, gender, age) VALUES (12, 'lin', 'male', 20);;
INSERT INTO demo.user (id, name, gender, age) VALUES (21, 'lin', 'male', 20);