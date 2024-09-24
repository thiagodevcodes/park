ALTER TABLE tb_vehicle_customer
DROP CONSTRAINT fk_vehiclecustomer_vehicle;

ALTER TABLE tb_vehicle_customer
ADD CONSTRAINT fk_vehiclecustomer_vehicle
FOREIGN KEY (id_vehicle)
REFERENCES tb_vehicle(id)
ON DELETE CASCADE;