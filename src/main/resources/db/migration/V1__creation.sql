CREATE TABLE IF NOT EXISTS `users` (
    id INT NOT NULL AUTO_INCREMENT,
    username VARCHAR(255),
    name VARCHAR(255),
    email VARCHAR(255),
    address VARCHAR(255),
    contact_info VARCHAR(255),
    birth_date DATE,
    states LONG VARCHAR,
    version TIMESTAMP,
    deleted BOOLEAN,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS restautants (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255),
    contact_info VARCHAR(255),
    info VARCHAR(255),
    address VARCHAR(255),
    opening_hours LONG VARCHAR,
    states LONG VARCHAR,
    version TIMESTAMP,
    deleted BOOLEAN,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS meals (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255),
    info VARCHAR(255),
    restaurant_id INT,
    price DECIMAL,
    addons LONG VARCHAR,
    states LONG VARCHAR,
    version TIMESTAMP,
    deleted BOOLEAN,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS orders (
    id INT NOT NULL AUTO_INCREMENT,
    restaurant_id INT,
    user_id INT,
    created DATE,
    approved DATE,
    delivered DATE,
    status VARCHAR(255),
    items LONG VARCHAR,
    states LONG VARCHAR,
    version TIMESTAMP,
    PRIMARY KEY (id)
);