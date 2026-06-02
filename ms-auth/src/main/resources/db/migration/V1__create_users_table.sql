CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

INSERT INTO users (username, password) VALUES ('admin', '1111');
INSERT INTO users (username, password) VALUES ('logistica', '2222');
INSERT INTO users (username, password) VALUES ('voluntario', '3333');
INSERT INTO users (username, password) VALUES ('donante', '4444');