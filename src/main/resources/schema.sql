create table if not exists c_user (
    id bigserial primary key,
    first_name varchar(100),
    last_name varchar(100),
    login varchar(100),
    password varchar(100),
    created_date timestamp
);
create sequence if not exists c_user_sequence start 1000 increment 1;

create table if not exists message (
    id bigserial primary key,
    content text,
    created_date timestamp,
    user_id bigint references c_user(id)
);
create sequence if not exists message_sequence start 1000 increment 1;

create table if not exists image (
    id bigserial primary key,
    title varchar(100) not null,
    price varchar(100) not null,
    path text,
    hash text,
    created_date timestamp not null,
    user_id bigint references c_user(id)
);
create sequence if not exists image_sequence start 1000 increment 1;

create table if not exists sold (
    id bigserial primary key,
    title varchar(100) not null,
    price varchar(100) not null,
    path text,
    hash text,
    c_like text,
    created_date timestamp not null,
    user_id bigint references c_user(id)
);
create sequence if not exists sold_sequence start 1000 increment 1;

create table if not exists account (
    id bigserial primary key,
    hash text,
    account1 text,
    account2 text,
    user_id bigint references c_user(id)
);
create sequence if not exists account_sequence start 1000 increment 1;