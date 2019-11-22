insert into customer values ('5555555555', 'Bob', '12345 123St', 1);
insert into customer values (null, 'Tom', null, 2);
insert into customer values (null, 'Jerry', null, 3);
insert into customer values (null, 'Frank', null, 4);

insert into vehicletype values('Economy', 'Sunroof', 200, 50, 20, 100, 25, 15);
insert into vehicletype values('Compact', null, null, null, null, null, null, null);
insert into vehicletype values('Mid-size', null, null, null, null, null, null, null);
insert into vehicletype values('Standard', null, null, null, null, null, null, null);
insert into vehicletype values('Full-size', null, null, null, null, null, null, null);
insert into vehicletype values('SUV', null, null, null, null, null, null, null);
insert into vehicletype values('Truck', null, null, null, null, null, null, null);

insert into branch values ('Cool Rentals', 'East Vancouver');
insert into branch values ('Cooler Rentals', 'West Vancouver');
insert into branch values ('Super Rentals', 'Coquitlam');
insert into branch values ('Bad Rentals', 'Maple Ridge');

insert into vehicle values (1, 111222, 'Honda', 'Civic', 1990, 'Red', 200000, 'Available', 'Economy', 'Cool Rentals', 'East Vancouver');
insert into vehicle values (2, 222333, 'Toyota', 'Corolla', 2001, 'Pink', 50000, 'Available', 'Economy', 'Cool Rentals', 'East Vancouver');
insert into vehicle values (3, 333444, 'Ford', 'F-150', 2007, 'Green', 50000, 'Rented', 'Truck', 'Cool Rentals', 'East Vancouver');
insert into vehicle values (4, 444555, 'Ford', 'Ranger', 2002, 'Blue', 50000, 'Maintenance', 'Truck', 'Cool Rentals', 'East Vancouver');
insert into vehicle values (5, 555666, 'Tesla', 'Model 3', 2019, 'Black', 10000, 'Available', 'Economy', 'Bad Rentals', 'Maple Ridge');
insert into vehicle values (6, 666777, 'Dodge', 'Charger', 2017, 'Black', 10000, 'Available', 'Standard', 'Bad Rentals', 'Maple Ridge');
insert into vehicle values (7, 777888, 'Chevrolet', 'Equinox', 2019, 'Black', 1000, 'Available', 'SUV', 'Bad Rentals', 'Maple Ridge');

insert into RESERVATION values (1234, 'Truck', 2, TO_TIMESTAMP('2019-11-20 10:00:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'), TO_TIMESTAMP('2019-12-25 08:15:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'));
insert into RESERVATION values (1235, 'Economy', 3, TO_TIMESTAMP('2019-12-01 12:00:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'), TO_TIMESTAMP('2019-12-30 10:30:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'));
insert into RESERVATION values (1236, 'Compact', 4, TO_TIMESTAMP('2019-10-25 12:00:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'), TO_TIMESTAMP('2020-01-15 15:30:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'));
insert into RESERVATION values (1237, 'SUV', 1, TO_TIMESTAMP('2019-11-25 12:00:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'), TO_TIMESTAMP('2020-11-26 12:00:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'));

insert into RENT values (1, 111222, 4, TO_TIMESTAMP('2019-12-01 12:00:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'), TO_TIMESTAMP('2019-12-25 10:30:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'), 200000, 'Frank', 1234567812345678, TO_DATE('2022-06-05', 'YYYY-MM-DD'), 1235);


insert into RENT values (2,555666,1,TO_TIMESTAMP('2019-11-25 12:00:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'),TO_TIMESTAMP('2019-11-26 12:00:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'),9293.32,'Tom',192304,TO_DATE('2020-06-05', 'YYYY-MM-DD'),1236);
