DROP TABLE IF EXISTS people;

CREATE TABLE people (
                        person_id BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
                        first_name VARCHAR(20),
                        last_name VARCHAR(20)
);
