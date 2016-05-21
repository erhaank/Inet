use agnesam; # Byt till eget användarnamn

drop table user;
drop table category;
drop table task;
drop table session;
drop table sessionTasks;
drop table workflow;

set names utf8;

create table user (
	id varchar(10) primary key,
	password integer
);

create table category (
	name varchar(15) primary key,
	userId varchar(10)
);

create table task (
	id integer auto_increment primary key,
	userId varchar(10),
	category varchar(15),
	minutes integer,
	name varchar(15),
	description varchar(30)
);

create table session (
	id integer auto_increment primary key,
	userId varchar(10),
	name varchar(15),
	description varchar(15)
);

create table sessionTasks (
	taskId Integer,
	sessionId Integer,
	amount integer,
	primary key(taskId, sessionId)
);

create table workflow (
	id integer auto_increment primary key,
	userId varchar(10),
	taskId integer,
	amount integer
);


