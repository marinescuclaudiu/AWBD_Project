DROP TABLE IF EXISTS address;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS user_profile;
DROP TABLE IF EXISTS review;
DROP TABLE IF EXISTS `order`;
DROP TABLE IF EXISTS order_product;
DROP TABLE IF EXISTS product_category;

CREATE TABLE Address (
                         Id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         Street INTEGER NOT NULL,
                         District VARCHAR(255) NOT NULL,
                         City INTEGER NOT NULL,
                         ZipCode INTEGER NOT NULL,
                         Country VARCHAR(255)
);

CREATE TABLE Product (
                         Id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         Name VARCHAR(255) NOT NULL,
                         Description TEXT,
                         Price INTEGER NOT NULL,
                         Photo VARCHAR(255),
                         Color VARCHAR(255)
);

CREATE TABLE Category (
                          Id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          Name VARCHAR(255) NOT NULL
);

CREATE TABLE User (
                      Id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      UserProfileID INTEGER,
                      Email VARCHAR(255) NOT NULL,
                      Password VARCHAR(255) NOT NULL,
                      FOREIGN KEY (UserProfileID) REFERENCES UserProfile(UserProfileID)
);

CREATE TABLE UserProfile (
                             Id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             FirstName VARCHAR(255),
                             LastName VARCHAR(255),
                             PhoneNumber VARCHAR(255)
);

CREATE TABLE Review (
                        Id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        ProductID INTEGER NOT NULL,
                        UserID INTEGER NOT NULL,
                        Rating INTEGER NOT NULL,
                        Content TEXT,
                        Date TIMESTAMP NOT NULL,
                        FOREIGN KEY (ProductID) REFERENCES Product(ProductID),
                        FOREIGN KEY (UserID) REFERENCES User(UserID)
);

CREATE TABLE Order (
                       Id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       UserID INTEGER NOT NULL,
                       AddressID INTEGER NOT NULL,
                       TotalAmount INTEGER NOT NULL,
                       OrderDate TIMESTAMP NOT NULL,
                       PaymentMethod ENUM('CreditCard', 'DebitCard', 'PayPal', 'Cash') NOT NULL,
                       FOREIGN KEY (UserID) REFERENCES User(UserID),
                       FOREIGN KEY (AddressID) REFERENCES Address(AddressID)
);

CREATE TABLE OrderProduct (
                              Id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              ProductID INTEGER NOT NULL,
                              OrderID INTEGER NOT NULL,
                              Quantity INTEGER NOT NULL,
                              TotalPrice INTEGER NOT NULL,
                              FOREIGN KEY (ProductID) REFERENCES Product(ProductID),
                              FOREIGN KEY (OrderID) REFERENCES Order(OrderID)
);

CREATE TABLE ProductCategory (
                                 ProductID INTEGER NOT NULL,
                                 CategoryID INTEGER NOT NULL,
                                 FOREIGN KEY (ProductID) REFERENCES Product(ProductID),
                                 FOREIGN KEY (CategoryID) REFERENCES Category(CategoryID),
                                 PRIMARY KEY (ProductID, CategoryID)
);