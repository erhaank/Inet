use agnesam; # Byt till eget användarnamn

drop table sessionTasks;
drop table inProgress;
drop table session;
drop table workflow;
drop table task;
drop table user;
drop table category;

set names utf8;

create table user (
	id varchar(16) primary key,
	password varchar(60)
);

create table category (
	id integer auto_increment primary key,
	name varchar(20),
	userId varchar(16)
);

create table task (
	id integer auto_increment primary key,
	userId varchar(16),
	categoryId integer,
	minutes integer,
	name varchar(20),
	description varchar(50),
	foreign key (userId) references user(id),
	foreign key (categoryId) references category(id)
);

create table session (
	id integer auto_increment primary key,
	userId varchar(16),
	name varchar(20),
	description varchar(50),
	foreign key (userId) references user(id)
);

create table sessionTasks (
	taskId Integer,
	sessionId Integer,
	amount integer,
	foreign key (taskId) references task(id),
	foreign key (sessionId) references session(id)
);

create table workflow (
	id integer auto_increment primary key,
	userId varchar(16),
	taskId integer,
	foreign key (userId) references user(id),
	foreign key (taskId) references task(id)
);

create table inProgress (
	userId varchar(16) not null unique,
	workflowId integer not null unique,
	endTime bigint,
	foreign key (userId) references user(id),
	foreign key (workflowId) references workflow(id)
);


