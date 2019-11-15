create table TimePeriod(
fromDate date,
fromTime date,
toDate date,
toTime date,
PRIMARY KEY (fromDate,fromTime,toDate,toTime));
create table Customer(
cellphone INTEGER,
name varchar(30),
address varchar(60),
dlicense INTEGER,
PRIMARY KEY (cellphone));
create table VehicleType(
vtname varchar(30),
features varchar(60),
wrate float,
drate float,
hrate float,
dirate float,
hirate float,
krate float,
PRIMARY KEY (vtname));
CREATE TABLE Reservation(
confNo INTEGER,
vtname    varchar(30),
cellphone INTEGER,
fromDate  date,
fromTime  date,
toDate    date,
toTime    date,
PRIMARY KEY (confNo),
foreign key (vtname) references VehicleType,
foreign key (cellphone) references Customer,
foreign key (fromDate,fromTime,toDate,toTime) references TimePeriod);
create TABLE ForRent(
vid INTEGER,
vlicense INTEGER,
make varchar(30),
model varchar(30),
year INTEGER,
color varchar(30),
odometer float,
status varchar(30) not null,
vtname varchar(30),
location varchar(30),
city varchar(30),
PRIMARY KEY (vid));
create table Rent(
rid INTEGER,
vid INTEGER,
cellphone INTEGER,
fromDate date,
fromTime date,
toDate date,
toTime date,
odometer float,
cardName varchar(30),
cardNo INTEGER,
expDate date,
confNo INTEGER,
PRIMARY KEY (rid),
FOREIGN KEY (vid) REFERENCES ForRent,
FOREIGN KEY (cellphone) REFERENCES Customer,
FOREIGN KEY (fromDate,fromTime,toDate,toTime) REFERENCES TimePeriod,
FOREIGN KEY (confNo) REFERENCES Reservation);
create table Branch(
location varchar(30),
city varchar(30),
PRIMARY KEY (location,city));
create table Vehicle(
vid INTEGER,
vlicense INTEGER,
make varchar(30),
model varchar(30),
year INTEGER,
color varchar(30),
odometer float,
status varchar(30) not null,
vtname varchar(30),
location varchar(30),
city varchar(30),
PRIMARY KEY (vid),
FOREIGN KEY (location,city) references Branch);