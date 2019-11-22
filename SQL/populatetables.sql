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
insert into branch values ('Yikes Rentals', 'Mission');

insert into vehicle values (1, 111222, 'Honda', 'Civic', 1990, 'Red', 200000,       'Available',    'Economy',  'Cool Rentals', 'East Vancouver');
insert into vehicle values (2, 222333, 'Toyota', 'Corolla', 2001, 'Pink', 50000,    'Available',    'Economy',  'Cool Rentals', 'East Vancouver');
insert into vehicle values (3, 333444, 'Ford', 'F-150', 2007, 'Green', 50000,       'Rented',       'Mid-size', 'Cool Rentals', 'East Vancouver');
insert into vehicle values (4, 444555, 'Ford', 'Ranger', 2002, 'Blue', 50000,       'Maintenance',  'Mid-size',  'Cool Rentals', 'East Vancouver');
insert into vehicle values (5, 555666, 'Tesla', 'Model 3', 2019, 'Black', 10000,    'Available',    'Truck',    'Bad Rentals',  'Maple Ridge');
insert into vehicle values (6, 666777, 'Dodge', 'Charger', 2017, 'Black', 10000,    'Available',    'Standard', 'Bad Rentals',  'Maple Ridge');
insert into vehicle values (7, 777888, 'Tesla', 'Cybertruck', 2021, 'Grey', 1000,   'Available',    'SUV',      'Bad Rentals',  'Maple Ridge');
insert into vehicle values (8, 888999, 'Acura', 'RDX', 2019, 'Black', 1000,         'Available',    'SUV',      'Super Rentals',  'Coquitlam');
insert into vehicle values (9, 999111, 'Toyota', 'Tundra', 2019, 'Black', 1000,     'Available',    'Truck',    'Super Rentals',  'Coquitlam');
insert into vehicle values (10, 111000, 'Ford', 'Fiesta', 1990, 'Green', 1000,      'Available',    'SUV',      'Cooler Rentals',  'West Vancouver');

insert into RESERVATION values (1231, 'Truck', 1, TO_TIMESTAMP('2019-11-20 13:00:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'), TO_TIMESTAMP('2019-12-25 08:15:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'));
insert into RESERVATION values (1232, 'Economy', 2, TO_TIMESTAMP('2019-12-01 12:00:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'), TO_TIMESTAMP('2019-12-30 10:30:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'));
insert into RESERVATION values (1233, 'Compact', 3, TO_TIMESTAMP('2019-10-25 12:00:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'), TO_TIMESTAMP('2020-01-15 15:30:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'));
insert into RESERVATION values (1234, 'SUV', 4, TO_TIMESTAMP('2019-11-25 12:00:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'), TO_TIMESTAMP('2020-11-26 12:00:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'));
insert into RESERVATION values (1235, 'SUV', 1, TO_TIMESTAMP('2019-11-25 12:00:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'), TO_TIMESTAMP('2020-11-26 12:00:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'));
insert into RESERVATION values (1236, 'SUV', 2, TO_TIMESTAMP('2019-11-25 12:00:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'), TO_TIMESTAMP('2020-11-26 12:00:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'));
insert into RESERVATION values (1237, 'SUV', 3, TO_TIMESTAMP('2019-11-25 12:00:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'), TO_TIMESTAMP('2020-11-26 12:00:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'));
insert into RESERVATION values (1238, 'SUV', 4, TO_TIMESTAMP('2019-11-25 12:00:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'), TO_TIMESTAMP('2020-11-26 12:00:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'));
insert into RESERVATION values (1239, 'SUV', 1, TO_TIMESTAMP('2019-11-25 12:00:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'), TO_TIMESTAMP('2020-11-26 12:00:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'));
insert into RESERVATION values (12310, 'SUV', 2, TO_TIMESTAMP('2019-11-25 12:00:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'), TO_TIMESTAMP('2020-11-26 12:00:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'));

insert into RENT values (90, 111222, 1, TO_TIMESTAMP('2019-10-10 01:00:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'), TO_TIMESTAMP('2019-01-10 08:15:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'), 200000, 'Mitch', 1234568, TO_DATE('2025-01-01', 'YYYY-MM-DD'), 1231);
insert into RENT values (91, 222333, 2, TO_TIMESTAMP('2019-10-10 02:00:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'), TO_TIMESTAMP('2019-01-10 08:15:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'), 200000, 'Mitch', 1234568, TO_DATE('2025-01-01', 'YYYY-MM-DD'), 1232);
insert into RENT values (92, 333444, 1, TO_TIMESTAMP('2019-10-10 12:01:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'), TO_TIMESTAMP('2019-01-10 08:15:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'), 200000, 'Mitch', 1234568, TO_DATE('2025-01-01', 'YYYY-MM-DD'), 1233);
insert into RENT values (93, 444555, 3, TO_TIMESTAMP('2019-10-10 08:00:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'), TO_TIMESTAMP('2020-01-10 10:30:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'), 200000, 'Mitch', 1234568, TO_DATE('2025-01-01', 'YYYY-MM-DD'), 1234);
insert into RENT values (94, 555666, 3, TO_TIMESTAMP('2019-10-10 18:00:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'), TO_TIMESTAMP('2020-01-10 10:30:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'), 200000, 'Mitch', 1234568, TO_DATE('2025-01-01', 'YYYY-MM-DD'), 1235);
insert into RENT values (95, 666777, 1, TO_TIMESTAMP('2019-10-10 16:00:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'), TO_TIMESTAMP('2019-01-10 08:15:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'), 200000, 'Mitch', 1234568, TO_DATE('2025-01-01', 'YYYY-MM-DD'), 1236);
insert into RENT values (96, 777888, 1, TO_TIMESTAMP('2019-10-10 12:00:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'), TO_TIMESTAMP('2019-01-10 08:15:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'), 200000, 'Mitch', 1234568, TO_DATE('2025-01-01', 'YYYY-MM-DD'), 1237);
insert into RENT values (97, 888999, 1, TO_TIMESTAMP('2019-10-10 15:00:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'), TO_TIMESTAMP('2019-01-10 08:15:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'), 200000, 'Mitch', 1234568, TO_DATE('2025-01-01', 'YYYY-MM-DD'), 1238);
insert into RENT values (98, 999111, 1, TO_TIMESTAMP('2019-10-10 11:59:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'), TO_TIMESTAMP('2019-01-10 08:15:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'), 200000, 'Mitch', 1234568, TO_DATE('2025-01-01', 'YYYY-MM-DD'), 1239);
insert into RENT values (99, 111000, 1, TO_TIMESTAMP('2019-10-09 23:59:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'), TO_TIMESTAMP('2019-01-10 08:15:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'), 200000, 'Mitch', 1234568, TO_DATE('2025-01-01', 'YYYY-MM-DD'), 12310);

insert into RETURN values (90, TO_TIMESTAMP('2019-11-11 01:00:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'), 9999, 1, 100);
insert into RETURN values (91, TO_TIMESTAMP('2019-11-11 02:00:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'), 9999, 1, 200);
insert into RETURN values (92, TO_TIMESTAMP('2019-11-11 03:00:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'), 9999, 1, 300);
insert into RETURN values (93, TO_TIMESTAMP('2019-11-11 04:00:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'), 9999, 1, 400);
insert into RETURN values (94, TO_TIMESTAMP('2019-11-11 05:00:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'), 9999, 1, 500);
insert into RETURN values (95, TO_TIMESTAMP('2019-11-11 06:00:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'), 9999, 1, 600);
insert into RETURN values (96, TO_TIMESTAMP('2019-11-11 07:00:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'), 9999, 1, 700);
insert into RETURN values (97, TO_TIMESTAMP('2019-11-11 08:00:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'), 9999, 1, 800);
insert into RETURN values (98, TO_TIMESTAMP('2019-11-11 09:00:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'), 9999, 1, 900);
insert into RETURN values (99, TO_TIMESTAMP('2019-11-12 00:01:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF'), 9999, 1, 1000);