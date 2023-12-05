--liquibase formatted sql

--changeset vera_p:1 endDelimiter:/

create table homes
(
    id                  int not null generated always as identity,
    owner_id            int not null,
    name                varchar not null,
    address             varchar,
    primary key (id)
);

create table rooms
(
    id                  int not null generated always as identity,
    name                varchar not null,
    home_id             int not null references homes on delete cascade,
    primary key (id)
);

create table outbox_messages
(
    id                  int not null generated always as identity,
    topic               varchar not null,
    message             varchar not null,
    primary key (id)
);