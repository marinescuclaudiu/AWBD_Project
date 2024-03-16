CREATE TABLE Address (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         street VARCHAR(50),
                         city VARCHAR(50),
                         district VARCHAR(50),
                         zip_code VARCHAR(50),
                         country VARCHAR(50)
);

CREATE TABLE Product (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(50),
                         description TEXT,
                         price DOUBLE,
                         photo LONGBLOB,
                         color VARCHAR(50)
);

CREATE TABLE Category (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(255)
);

CREATE TABLE `user` (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        email VARCHAR(255),
                        password VARCHAR(255)
);

CREATE TABLE user_profile (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              user_id BIGINT,
                              first_name VARCHAR(255),
                              last_name VARCHAR(255),
                              phone_number VARCHAR(255),
                              FOREIGN KEY (user_id) REFERENCES `user`(id)

);


CREATE TABLE Review (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        product_id BIGINT,
                        user_id BIGINT,
                        rating DOUBLE,
                        content TEXT,
                        date DATE,
                        FOREIGN KEY (product_id) REFERENCES Product(id),
                        FOREIGN KEY (user_id) REFERENCES `user`(id)
);

CREATE TABLE `order` (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         user_id BIGINT,
                         address_id BIGINT,
                         total_amount DOUBLE,
                         order_date DATE,
                         payment_method VARCHAR(20),
                         FOREIGN KEY (user_id) REFERENCES `user`(id),
                         FOREIGN KEY (address_id) REFERENCES Address(id)
);

CREATE TABLE order_product (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               product_id BIGINT,
                               order_id BIGINT,
                               quantity INTEGER,
                               total_price DOUBLE,
                               FOREIGN KEY (product_id) REFERENCES Product(id),
                               FOREIGN KEY (order_id) REFERENCES `order`(id)
);

CREATE TABLE product_category (
                                  product_id BIGINT,
                                  category_id BIGINT,
                                  FOREIGN KEY (product_id) REFERENCES Product(id),
                                  FOREIGN KEY (category_id) REFERENCES Category(id),
                                  PRIMARY KEY (product_id, category_id)
);