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
