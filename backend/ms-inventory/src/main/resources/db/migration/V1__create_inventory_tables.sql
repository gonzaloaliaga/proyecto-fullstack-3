CREATE TABLE collection_centers (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    region VARCHAR(255) NOT NULL,
    capacity INTEGER NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE
);
 
CREATE TABLE inventory_items (
    id BIGSERIAL PRIMARY KEY,
    collection_center_id BIGINT NOT NULL REFERENCES collection_centers(id),
    resource VARCHAR(255) NOT NULL,
    quantity INTEGER NOT NULL DEFAULT 0,
    unit VARCHAR(50) NOT NULL,
    last_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
 
INSERT INTO collection_centers (name, address, region, capacity, active)
VALUES ('Centro de Acopio Central', 'Av. Libertador Bernardo O''Higgins 1234, Santiago', 'Metropolitana', 5000, TRUE);
 
INSERT INTO collection_centers (name, address, region, capacity, active)
VALUES ('Centro de Acopio Maipú', 'Av. Pajaritos 4500, Maipú', 'Metropolitana', 2000, TRUE);
 
INSERT INTO inventory_items (collection_center_id, resource, quantity, unit)
VALUES (1, 'Ropa de invierno', 50, 'unidades');
 
INSERT INTO inventory_items (collection_center_id, resource, quantity, unit)
VALUES (1, 'Alimentos no perecibles', 200, 'kg');
 
INSERT INTO inventory_items (collection_center_id, resource, quantity, unit)
VALUES (2, 'Insumos médicos básicos', 30, 'cajas');
