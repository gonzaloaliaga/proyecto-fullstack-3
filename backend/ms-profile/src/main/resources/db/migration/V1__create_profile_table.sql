CREATE TABLE profiles (
    id BIGSERIAL PRIMARY KEY,
    role VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    address VARCHAR(255) NOT NULL,
    run VARCHAR(255) UNIQUE NOT NULL
);

INSERT INTO profiles (role, email, address, run) VALUES ('ADMIN', 'admin@donaton.cl', 'Calle Con Asfalto 123', '220330591');
INSERT INTO profiles (role, email, address, run) VALUES ('LOGISTIC', 'logistic@donaton.cl', 'Calle Sin Asfalto 123', '218854608');
INSERT INTO profiles (role, email, address, run) VALUES ('VOLUNTEER', 'volunteer@gmail.com', 'Depa Ruidoso 456', '220648152');
INSERT INTO profiles (role, email, address, run) VALUES ('DONOR', 'donor@gmail.cl', 'Depa Ruidoso 456', '123456789');