DROP TABLE IF EXISTS company;
CREATE TABLE company (
  id   BIGINT       NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
);

DROP TABLE IF EXISTS person;
CREATE TABLE person (
  id   BIGINT       NOT NULL AUTO_INCREMENT,
  company_id BIGINT NOT NULL,
  name VARCHAR(255) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (company_id) REFERENCES company(id)
);

INSERT INTO company VALUES
  (1, 'company1'),
  (2, 'company2'),
  (3, 'company3'),
  (4, 'company4'),
  (5, 'company5'),
  (6, 'company6'),
  (7, 'company7'),
  (8, 'company8'),
  (9, 'company9'),
  (10, 'company10'),
  (11, 'company11'),
  (12, 'company12'),
  (13, 'company13'),
  (14, 'company14'),
  (15, 'company15'),
  (16, 'company16'),
  (17, 'company17'),
  (18, 'company18'),
  (19, 'company19'),
  (20, 'company20')
;
INSERT INTO company(name) select name from company;

INSERT INTO person VALUES
  (1, 1, 'person1'),
  (2, 1, 'person2'),
  (3, 2, 'person3'),
  (4, 2, 'person4'),
  (5, 3, 'person5'),
  (6, 3, 'person6'),
  (7, 4, 'person7'),
  (8, 4, 'person8'),
  (9, 5, 'person9'),
  (10, 5, 'person10'),
  (11, 6, 'person11'),
  (12, 6, 'person12'),
  (13, 7, 'person13'),
  (14, 7, 'person14'),
  (15, 8, 'person15'),
  (16, 8, 'person19'),
  (17, 9, 'person17'),
  (18, 9, 'person18'),
  (19, 10, 'person19'),
  (20, 10, 'person20'),
  (21, 11, 'person21'),
  (22, 11, 'person22'),
  (23, 12, 'person23'),
  (24, 12, 'person24'),
  (25, 13, 'person25'),
  (26, 13, 'person26'),
  (27, 14, 'person27'),
  (28, 14, 'person28'),
  (29, 15, 'person29'),
  (30, 15, 'person30'),
  (31, 16, 'person31'),
  (32, 16, 'person32'),
  (33, 17, 'person33'),
  (34, 17, 'person34'),
  (35, 18, 'person35'),
  (36, 18, 'person36'),
  (37, 19, 'person37'),
  (38, 19, 'person38'),
  (39, 20, 'person39'),
  (40, 20, 'person40')
;

INSERT INTO person(company_id, name) SElECT company_id, name from person;