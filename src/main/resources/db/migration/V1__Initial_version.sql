CREATE TABLE transactions (
    id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    date TIMESTAMP NOT NULL,
    amount NUMERIC NOT NULL,
    description VARCHAR(100) NOT NULL
);

INSERT INTO transactions (date, amount, description) VALUES ('1999-01-08 04:05:06', 42, 'Donuts');