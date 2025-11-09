CREATE SEQUENCE IF NOT EXISTS id_sequence START WITH 1 INCREMENT BY 1;
alter sequence id_sequence owned by user_.id;