insert into category(id, name) values(1, 'beds');
insert into category(id, name) values(2, 'chairs');
insert into category(id, name) values(3, 'tables');
insert into category(id, name) values(4, 'couches');

insert into product (id, name, description, price, color) values (1, 'Scaun de birou ergonomic', 'Avola scaun de birou', '292', 'black');
insert into product (id, name, description, price, color) values (2, 'Scaun de birou ergonomic', 'Arka scaun de birou profesional', '799', 'gray');

insert into product_category (product_id, category_id) values(1,2);
insert into product_category (product_id, category_id) values(2,2);

insert into `user` (id, email) values (1, 'john_snow@gmail.com');
insert into `user` (id, email) values (2, 'adriana_lima@gmail.com');
insert into `user` (id, email) values (3, 'alessandra_ambrosio@gmail.com');
insert into `user` (id, email) values (4, 'david_north@gmail.com');

insert into user_profile (id, user_id, first_name, last_name) values (1, 1, 'John', 'Snow');

insert into review (id, product_id, user_id, rating) values (1, 2, 1, 5.0);