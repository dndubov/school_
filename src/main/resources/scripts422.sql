
CREATE TABLE car (
    id SERIAL PRIMARY KEY,
    brand VARCHAR(255) NOT NULL,
    model VARCHAR(255) NOT NULL,
    price NUMERIC(12, 2) NOT NULL
);

CREATE TABLE person (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    age INT NOT NULL,
    has_license BOOLEAN NOT NULL,
    car_id INT,
    CONSTRAINT fk_car
        FOREIGN KEY(car_id)
        REFERENCES car(id)
        ON DELETE SET NULL
);

