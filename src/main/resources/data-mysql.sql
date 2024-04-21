insert into category(id, name) values(1, 'beds');
insert into category(id, name) values(2, 'chairs');
insert into category(id, name) values(3, 'tables');
insert into category(id, name) values(4, 'couches');

insert into product (id, name, description, price, color) values (1, 'Scaun de birou ergonomic', 'Avola scaun de birou', '292', 'black');
insert into product (id, name, description, price, color) values (2, 'Scaun de birou ergonomic', 'Arka scaun de birou profesional', '799', 'gray');

insert into product_category (product_id, category_id) values(1,2);
insert into product_category (product_id, category_id) values(2,2);