INSERT INTO worker (id, name)
VALUES (1, 'Vanya'),
       (2, 'Sasha');

INSERT INTO products (worker_id, items, price, quantity)
VALUES (1, 'Молоко', 50, 20),
       (2, 'Хлеб', 30, 50),
       (3, 'Масло', 100, 20),
       (4, 'Яблоки', 50, 200);