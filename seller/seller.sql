CREATE DATABASE IF NOT EXISTS seller;
USE seller;

DROP TABLE IF EXISTS Watchlists;
DROP TABLE IF EXISTS Order_Products;
DROP TABLE IF EXISTS Products;
DROP TABLE IF EXISTS Orders;
DROP TABLE IF EXISTS Users;

CREATE TABLE IF NOT EXISTS Users (
    user_id int auto_increment PRIMARY KEY,
    username varchar(100) NOT NULL UNIQUE,
    password varchar(100) NOT NULL UNIQUE,
    email varchar(100) NOT NULL UNIQUE,
    admin boolean NOT NULL
);

INSERT INTO Users (username, password, email, admin) VALUES
                                    ('user1', 'pass1', 'jhan@gmail.com', false),
                                    ('user2', 'pass2', 'cyang@gmail.com', false),
                                    ('user3', 'pass3', 'lliu@gmail.com',true),
                                    ('admin', '123', 'admin@xxx.com', true);

CREATE TABLE IF NOT EXISTS Orders (
     order_id int auto_increment PRIMARY KEY,
     user_id int,
     status varchar(100) NOT NULL,
     time_created timestamp NOT NULL,
     FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO Orders (user_id, status, time_created) VALUES
									  (1, 'processing', timestamp('2025-05-12 12:00:00')),
									  (1, 'completed', timestamp('2025-05-12 12:00:01')),
									  (1, 'completed', timestamp('2025-05-12 12:00:02')),
									  (2, 'completed', timestamp('2025-05-12 12:00:00')),
									  (2, 'canceled', timestamp('2025-05-12 12:00:01'));

CREATE TABLE IF NOT EXISTS Products (
    product_id int auto_increment PRIMARY KEY,
    name varchar(100) NOT NULL,
    description varchar(100) NOT NULL,
    wholesale_price double NOT NULL,
    retail_price double NOT NULL,
    quantity int NOT NULL
);

INSERT INTO Products (name, description, wholesale_price, retail_price, quantity) VALUES
																			('whole chicken', 'air-chilled', 5.00, 7.00, 1000),
																			('ground beef', 'angus', 3.00, 5.00, 10),
                                                                            ('fancy pork', 'Iberico', 15.00, 20.00, 0);

CREATE TABLE IF NOT EXISTS Order_Products (
    order_product_id int auto_increment PRIMARY KEY,
    order_id int,
    product_id int,
    retail_price_at_time_of_purchase double NOT NULL,
    wholesale_price_at_time_of_purchase double NOT NULL,
    quantity int NOT NULL,
    profit double AS(quantity * (retail_price_at_time_of_purchase - wholesale_price_at_time_of_purchase)) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES Orders(order_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (product_id) REFERENCES Products(product_id) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO Order_Products (order_id, 
							product_id, 
                            wholesale_price_at_time_of_purchase,
                            retail_price_at_time_of_purchase, 
                            quantity)
VALUES
	(1, 1, 5.00, 7.00, 5),
	(1, 2, 3.00, 5.00, 4),
	(2, 1, 5.00, 7.00, 3),
	(2, 3, 15.00, 20.00, 7),
	(3, 1, 5.00, 7.00, 1),
	(3, 2, 3.00, 5.00, 70),
	(4, 3, 15.00, 20.00, 1),
	(5, 2, 3.00, 5.00, 9);


CREATE TABLE IF NOT EXISTS Watchlists (
	watchlist_id int auto_increment PRIMARY KEY,
    user_id int,
    product_id int,
    in_stock boolean NOT NULL,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (product_id) REFERENCES Products(product_id) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO Watchlists(user_id, product_id, in_stock) VALUES
													  (1, 1, true),
                                                      (1, 3, false),
                                                      (1, 2, true),
                                                      (2, 2, true),
                                                      (2, 3, false);

SELECT op.product_id, SUM(op.quantity) AS total_sold
FROM Orders o NATURAL JOIN Order_Products op
WHERE o.status = 'completed'
GROUP BY op.product_id
ORDER BY total_sold DESC;



/*SELECT product_id
FROM Orders NATURAL JOIN Order_Products
WHERE user_id = 1
GROUP BY product_id
ORDER BY COUNT(product_id) DESC
LIMIT 3;

SELECT op.product_id
FROM Orders o NATURAL JOIN Order_Products op
WHERE user_id = 1
ORDER BY o.time_created DESC
LIMIT 3;

SELECT op.product_id
FROM Orders o NATURAL JOIN Order_Products op;

ALTER TABLE Order_Products
ADD profit double AS (quantity * (retail_price_at_time_of_purchase - wholesale_price_at_time_of_purchase));

SELECT * FROM Orders JOIN Order_Products;

SELECT product_id, SUM(profit) AS total_profit
FROM Orders NATURAL JOIN Order_Products
WHERE Orders.status != 'canceled'
GROUP BY product_id
ORDER BY total_profit DESC;

SELECT product_id, COUNT(product_id)
FROM Orders NATURAL JOIN Order_Products
WHERE Orders.status = 'completed'
GROUP BY product_id
ORDER BY COUNT(product_id) DESC
LIMIT 3;*/

