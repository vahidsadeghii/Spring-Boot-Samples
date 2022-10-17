create table talk
(
    id int auto_increment not null ,
    create_date datetime null,
    title varchar(200) null,
    author varchar(100) null,
    schedule_date varchar(50) null,
    view_count bigint default 0 null,
    like_count bigint default 0 null,
    link varchar(1024) null,
    constraint talk_pk
        primary key (id)
);