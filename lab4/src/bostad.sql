use agnesam; # Byt till eget användarnamn

drop table bostader; # Om det finns en tidigare databas

set names utf8;

create table bostader (
lan varchar(64),
objekttyp varchar(64),
adress varchar(64),
area float,
rum int,
pris float,
avgift float
);


insert into bostader values ('Stockholm','Bostadsrätt','Polhemsgatan 1',30,1,1000000,1234);

insert into bostader values ('Stockholm','Bostadsrätt','Polhemsgatan 2',60,2,2000000,2345);

insert into bostader values ('Stockholm','Villa','Storgatan 1',130,5,1000000,3456);

insert into bostader values ('Stockholm','Villa','Storgatan 2',160,6,1000000,3456);

insert into bostader values ('Uppsala','Bostadsrätt','Gröna gatan 1',30,1,500000,1234);

insert into bostader values ('Uppsala','Bostadsrätt','Gröna gatan 2',60,2,1000000,2345);

insert into bostader values ('Uppsala','Villa','Karlaplan 8',130,5,1000000,3456);

insert into bostader values ('Uppsala','Villa','Bro 9',160,6,1000000,3456);

insert into bostader values ('Uppsala','Bostadsrätt','Karlberg 89',90,1,500000,1299);

insert into bostader values ('Uppsala','Bostadsrätt','Wintergatan 78',60,2,8000000,2900);

insert into bostader values ('Uppsala','Villa','Öberg 9',190,5,7000000,3756);

insert into bostader values ('Örebro','Villa','Kalmar 9',120,8,2000000,3400);

insert into bostader values ('Örebro','Villa','Öland 90',160,6,90000000,3456);

insert into bostader values ('Örebro','Bostadsrätt','Karlberg 89',90,1,500000,1299);

insert into bostader values ('Örebro','Bostadsrätt','Wat 89',60,2,8000000,2900);

insert into bostader values ('Örebro','Villa','Hallåvägen 67',190,5,7009000,3756);

insert into bostader values ('Örebro','Villa','Kovägen 64',120,8,2000000,3400);


SELECT * FROM bostader