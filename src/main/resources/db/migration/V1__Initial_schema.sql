CREATE TABLE account (
    id varchar(30) PRIMARY KEY,
    name varchar(100) NOT NULL UNIQUE
);

CREATE TYPE category_kind AS ENUM ('income_or_expense', 'internal_transfer');

CREATE TABLE category (
    id integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name varchar(100) NOT NULL UNIQUE,
    kind category_kind NOT NULL
);

CREATE TABLE transaction (
    id integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    account varchar(100) REFERENCES account,
    category integer REFERENCES category,
    date timestamp with time zone NOT NULL,
    amount numeric(12, 2) NOT NULL,
    description varchar(500) NOT NULL,
    raw varchar(1000) NOT NULL UNIQUE,
    UNIQUE(account, date, amount, description)
);
