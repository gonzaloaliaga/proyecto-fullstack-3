CREATE TABLE donations (
    id BIGSERIAL PRIMARY KEY,
    resource VARCHAR(255) NOT NULL,
    quantity INTEGER NOT NULL,
    origin VARCHAR(255) NOT NULL,
    donation_date DATE NOT NULL,
    collection_center_id BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING'
);
 
INSERT INTO donations (resource, quantity, origin, donation_date, collection_center_id, status)
VALUES ('Ropa de invierno', 50, 'Empresa TextilSur SpA', '2026-03-10', 1, 'RECEIVED');
 
INSERT INTO donations (resource, quantity, origin, donation_date, collection_center_id, status)
VALUES ('Alimentos no perecibles', 200, 'Donante individual - Juan Pérez', '2026-03-12', 1, 'RECEIVED');
 
INSERT INTO donations (resource, quantity, origin, donation_date, collection_center_id, status)
VALUES ('Insumos médicos básicos', 30, 'Municipalidad de Maipú', '2026-03-15', 2, 'PENDING');
