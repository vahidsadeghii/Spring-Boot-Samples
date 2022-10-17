create database tedtalk;
create user 'tedtalk'@'%' identified by '123456';
grant all on tedtalk.* to 'tedtalk'@'%';