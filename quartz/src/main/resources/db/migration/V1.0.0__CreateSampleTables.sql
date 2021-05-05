DROP TABLE IF EXISTS TASKS;
DROP SEQUENCE IF EXISTS hibernate_sequence;

CREATE SEQUENCE hibernate_sequence start 1 increment 1;

CREATE TABLE TASKS
(
    id int8 NOT NULL,
    createdAt TIMESTAMP,
    PRIMARY KEY (id)
);