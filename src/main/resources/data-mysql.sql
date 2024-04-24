insert into category(id, name) values(1, 'beds');
insert into category(id, name) values(2, 'chairs');
insert into category(id, name) values(3, 'tables');
insert into category(id, name) values(4, 'couches');

insert into product (id, name, description, price, color) values (1, 'Scaun de birou ergonomic', 'Avola scaun de birou', '292', 'black');
insert into product (id, name, description, price, color) values (2, 'Sofa Modular', 'Comfortable modular sofa', '1199', 'gray');
insert into product (id, name, description, price, color) values (3, 'Coffee Table Wood', 'Solid wood coffee table', '450', 'brown');
insert into product (id, name, description, price, color) values (4, 'Desk Lamp', 'LED desk lamp', '89', 'white');
insert into product (id, name, description, price, color) values (5, 'Bookshelf', 'Spacious wooden bookshelf', '320', 'oak');
insert into product (id, name, description, price, color) values (6, 'Armchair Recliner', 'Leather recliner armchair', '760', 'black');
insert into product (id, name, description, price, color) values (7, 'Dining Set', '6 piece dining set', '1350', 'white');
insert into product (id, name, description, price, color) values (8, 'Outdoor Lounge Chair', 'Adjustable outdoor lounge chair', '210', 'green');
insert into product (id, name, description, price, color) values (9, 'Bar Stool', 'Modern kitchen bar stool', '110', 'red');
insert into product (id, name, description, price, color) values (10, 'TV Stand', 'Contemporary TV stand', '299', 'black');
insert into product (id, name, description, price, color) values (11, 'Wall Mirror', 'Decorative wall mirror', '120', 'silver');
insert into product (id, name, description, price, color) values (12, 'Office Chair', 'Mesh back office chair', '350', 'blue');
insert into product (id, name, description, price, color) values (13, 'Bed Frame', 'Queen size bed frame', '499', 'black');
insert into product (id, name, description, price, color) values (14, 'Nightstand', 'Wooden nightstand with drawers', '130', 'cherry');
insert into product (id, name, description, price, color) values (15, 'Wardrobe', 'Large double door wardrobe', '880', 'white');
insert into product (id, name, description, price, color) values (16, 'Patio Set', '4 piece patio set', '650', 'teak');
insert into product (id, name, description, price, color) values (17, 'Study Desk', 'Compact study desk', '220', 'maple');
insert into product (id, name, description, price, color) values (18, 'Rocking Chair', 'Wooden rocking chair', '170', 'white');
insert into product (id, name, description, price, color) values (19, 'Accent Cabinet', 'Antique style accent cabinet', '320', 'green');
insert into product (id, name, description, price, color) values (20, 'Folding Chair', 'Portable folding chair', '45', 'black');
insert into product (id, name, description, price, color) values (21, 'Leather Ottoman', 'Luxurious leather ottoman', '189', 'brown');

insert into product_category (product_id, category_id) values(1,2);
insert into product_category (product_id, category_id) values(2,2);