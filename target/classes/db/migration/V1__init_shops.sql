CREATE TABLE shops (
                       id BIGSERIAL PRIMARY KEY,
                       name VARCHAR(100) NOT NULL,
                       address VARCHAR(200),
                       phone VARCHAR(20),
                       email VARCHAR(100),
                       opening_date DATE,
                       active BOOLEAN DEFAULT TRUE,
                       employee_count INTEGER,
                       area DECIMAL(10,2),
                       category VARCHAR(50),
                       revenue DECIMAL(15,2) DEFAULT 0
);

CREATE INDEX idx_shops_name ON shops(name);
CREATE INDEX idx_shops_category ON shops(category);

INSERT INTO shops (name, address, phone, email, opening_date, active, employee_count, area, category, revenue) VALUES
                                                                                                                   ('Пятёрочка', 'ул. Ленина, 10', '+79991234567', 'pyaterochka@example.com', '2020-01-15 09:00:00', true, 15, 200.5, 'продукты', 1500000.00),
                                                                                                                   ('Магнит', 'ул. Советская, 25', '+79997654321', 'magnit@example.com', '2019-05-20 08:30:00', true, 12, 180.0, 'продукты', 1200000.00),
                                                                                                                   ('Спортмастер', 'пр. Мира, 5', '+79998887766', 'sportmaster@example.com', '2021-03-10 10:00:00', true, 8, 300.0, 'спорттовары', 800000.00),
                                                                                                                   ('М.Видео', 'ул. Пушкина, 15', '+79995554433', 'mvideo@example.com', '2018-07-05 09:30:00', true, 20, 450.0, 'электроника', 2500000.00),
                                                                                                                   ('Л''Этуаль', 'ТЦ "Мега", 2 этаж', '+79992221100', 'letual@example.com', '2022-02-14 11:00:00', true, 10, 150.0, 'косметика', 900000.00);