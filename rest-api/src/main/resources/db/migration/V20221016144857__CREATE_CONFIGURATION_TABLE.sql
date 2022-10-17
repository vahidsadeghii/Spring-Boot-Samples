create table configuration
(
    conf_key varchar(50) not null,
    conf_value varchar(50) null,
    constraint configuration_pk
        primary key (conf_key)
);