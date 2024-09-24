package com.sys.park.app.repositories;

import java.util.Optional;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sys.park.app.models.CustomerVehicleModel;

@Repository
public interface CustomerVehicleRepository extends JpaRepository<CustomerVehicleModel, Long> {
    Page<CustomerVehicleModel> findByIdCustomer(Pageable pageable, Long idCustomer);
    Optional<CustomerVehicleModel> findByIdCustomerAndIdVehicle(Long idCustomer, Long idVehicle);
    Boolean existsByIdCustomerAndIdVehicle(Long idCustomer, Long idVehicle);
}
