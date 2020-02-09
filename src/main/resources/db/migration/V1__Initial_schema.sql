CREATE TABLE account (
    id integer PRIMARY KEY,
    name varchar(100) NOT NULL UNIQUE
);

CREATE TABLE category (
    id integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name varchar(100) NOT NULL UNIQUE
);

CREATE TABLE transaction (
    id integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    account integer REFERENCES account,
    category integer REFERENCES category,
    date timestamp with time zone NOT NULL,
    amount numeric(6, 4) NOT NULL,
    description varchar(100) NOT NULL,
    raw varchar(1000) NOT NULL UNIQUE,
    UNIQUE(account, date, amount, description)
);
