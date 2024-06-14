package com.sys.park.app.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sys.park.app.models.CustomerVehicleModel;

@Repository
public interface CustomerVehicleRepository extends JpaRepository<CustomerVehicleModel, Integer> {
    List<CustomerVehicleModel> findByIdCustomer(Integer idCustomer);
    Optional<CustomerVehicleModel> findByIdCustomerAndIdVehicle(Integer idCustomer, Integer idVehicle);
    Boolean existsByIdCustomerAndIdVehicle(Integer idCustomer, Integer idVehicle);
}
