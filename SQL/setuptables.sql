create table Customer(
cellphone INTEGER,
name varchar(30),
address varchar(60),
dlicense INTEGER,
PRIMARY KEY (dlicense));

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
dlicense INTEGER,
fromDateTime  timestamp,
toDateTime    timestamp,
PRIMARY KEY (confNo),
foreign key (vtname) REFERENCES VehicleType,
foreign key (dlicense) REFERENCES Customer);

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
PRIMARY KEY (vlicense),
FOREIGN KEY (location,city) REFERENCES Branch);

create table Rent(
rid INTEGER,
vlicense INTEGER,
dlicense INTEGER,
fromDateTime timestamp,
toDateTime timestamp,
odometer float,
cardName varchar(30),
cardNo INTEGER,
expDate date,
confNo INTEGER,
PRIMARY KEY (rid),
FOREIGN KEY (vlicense) REFERENCES Vehicle,
FOREIGN KEY (dlicense) REFERENCES Customer,
FOREIGN KEY (confNo) REFERENCES Reservation);

create table Return(
rid INTEGER,
returnDateTime timestamp,
odometer float,
fulltank number(1),
value INTEGER,
PRIMARY KEY (rid),
FOREIGN KEY (rid) REFERENCES Rent);