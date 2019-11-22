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
insert into RESERVATION values (1238, 'SUV', 1, TO_TIMESTAMP('2019-11-25 12:00:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'), TO_TIMESTAMP('2020-11-26 12:00:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'));
insert into RESERVATION values (1239, 'SUV', 1, TO_TIMESTAMP('2019-11-25 12:00:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'), TO_TIMESTAMP('2020-11-26 12:00:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'));


insert into RENT values (90, 111222, 1, TO_TIMESTAMP('2019-11-21 15:00:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'), TO_TIMESTAMP('2019-01-10 08:15:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'), 200000, 'Mitch', 1234568, TO_DATE('2025-01-01', 'YYYY-MM-DD'), 1234);
insert into RENT values (91, 222333, 2, TO_TIMESTAMP('2019-11-21 10:00:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'), TO_TIMESTAMP('2019-01-10 08:15:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'), 200000, 'Mitch', 1234568, TO_DATE('2025-01-01', 'YYYY-MM-DD'), 1235);
insert into RENT values (92, 333444, 1, TO_TIMESTAMP('2019-01-21 09:00:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'), TO_TIMESTAMP('2019-01-10 08:15:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'), 200000, 'Mitch', 1234568, TO_DATE('2025-01-01', 'YYYY-MM-DD'), 1236);
insert into RENT values (93, 555666, 3, TO_TIMESTAMP('2019-01-03 08:00:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'), TO_TIMESTAMP('2020-01-10 10:30:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'), 200000, 'Mitch', 1234568, TO_DATE('2025-01-01', 'YYYY-MM-DD'), 1237);
insert into RENT values (94, 666777, 3, TO_TIMESTAMP('2019-11-21 18:00:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'), TO_TIMESTAMP('2020-01-10 10:30:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'), 200000, 'Mitch', 1234568, TO_DATE('2025-01-01', 'YYYY-MM-DD'), 1238);
insert into RENT values (95, 111222, 1, TO_TIMESTAMP('2019-11-22 15:00:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'), TO_TIMESTAMP('2019-01-10 08:15:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'), 200000, 'Mitch', 1234568, TO_DATE('2025-01-01', 'YYYY-MM-DD'), 1239);


insert into RETURN values (90, TO_TIMESTAMP('2019-10-09 10:00:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'), 9999, 1, 400);
insert into RETURN values (91, TO_TIMESTAMP('2019-10-10 10:00:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'), 9999, 1, 500);
insert into RETURN values (92, TO_TIMESTAMP('2019-10-10 15:00:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'), 9999, 1, 600);
insert into RETURN values (93, TO_TIMESTAMP('2019-10-10 10:00:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'), 9999, 1, 700);
insert into RETURN values (94, TO_TIMESTAMP('2019-10-11 10:00:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'), 9999, 1, 800);