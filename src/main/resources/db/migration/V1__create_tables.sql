-- Миграция Flyway: создание таблиц recipe, ingredients и statistics

CREATE TABLE recipe (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        water INTEGER NOT NULL,
                        coffee INTEGER NOT NULL,
                        milk INTEGER NOT NULL
);

CREATE TABLE ingredients (
                             id SERIAL PRIMARY KEY,
                             name VARCHAR(255) NOT NULL,
                             quantity INTEGER NOT NULL
);

CREATE TABLE statistics (
                            id SERIAL PRIMARY KEY,
                            name VARCHAR(255) NOT NULL,
                            count INTEGER NOT NULL
);
