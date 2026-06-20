CREATE TABLE needs (
    id BIGSERIAL PRIMARY KEY,
    resource VARCHAR(255) NOT NULL,
    quantity INTEGER NOT NULL,
    location VARCHAR(255) NOT NULL,
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    reported_by VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'REPORTED',
    reported_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
 
CREATE TABLE transports (
    id BIGSERIAL PRIMARY KEY,
    vehicle_plate VARCHAR(20) NOT NULL,
    vehicle_type VARCHAR(50) NOT NULL,
    driver_name VARCHAR(255) NOT NULL,
    capacity INTEGER NOT NULL,
    available BOOLEAN NOT NULL DEFAULT TRUE
);
 
CREATE TABLE shipments (
    id BIGSERIAL PRIMARY KEY,
    donation_id BIGINT,
    need_id BIGINT REFERENCES needs(id),
    transport_id BIGINT REFERENCES transports(id),
    origin_center_id BIGINT NOT NULL,
    destination VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PLANNED',
    scheduled_date DATE NOT NULL,
    delivered_at TIMESTAMP
);
 
INSERT INTO transports (vehicle_plate, vehicle_type, driver_name, capacity, available)
VALUES ('ABCD12', 'Camión', 'Pedro González', 1000, TRUE);
 
INSERT INTO transports (vehicle_plate, vehicle_type, driver_name, capacity, available)
VALUES ('EFGH34', 'Furgón', 'María Soto', 400, TRUE);
 
INSERT INTO needs (resource, quantity, location, latitude, longitude, reported_by, status)
VALUES ('Agua potable', 500, 'Comuna de Til Til', -33.0833, -70.9333, 'Municipalidad de Til Til', 'REPORTED');
 
INSERT INTO needs (resource, quantity, location, latitude, longitude, reported_by, status)
VALUES ('Frazadas', 100, 'Sector Bajos de Mena, Puente Alto', -33.6167, -70.5833, 'Voluntario Carlos Reyes', 'REPORTED');
 
INSERT INTO shipments (donation_id, need_id, transport_id, origin_center_id, destination, status, scheduled_date)
VALUES (1, 1, 1, 1, 'Comuna de Til Til', 'PLANNED', '2026-04-01');
