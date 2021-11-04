--liquibase formatted sql

--changeset elvina:1

insert into account1 (amount, version) values (800, 0);
insert into account1 (amount, version) values (500, 0);