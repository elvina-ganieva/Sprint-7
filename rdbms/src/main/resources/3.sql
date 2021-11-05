--liquibase formatted sql

--changeset elvina:3

alter table account1 add constraint amount_check check (amount >= 0);