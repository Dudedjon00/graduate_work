CREATE TABLE owners
(
    id   INTEGER PRIMARY KEY AUTO_INCREMENT,
    name TEXT NOT NULL
);
CREATE TABLE flats
(
    id       INTEGER PRIMARY KEY AUTO_INCREMENT,
    owner_id INTEGER NOT NULL REFERENCES owners,
    district TEXT    NOT NULL,
    price    INTEGER NOT NULL CHECK (price > 0),
    rooms    INTEGER NOT NULL DEFAULT 1 CHECK (rooms > 0)


);