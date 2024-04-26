package com.sys.park.app.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sys.park.app.models.VehicleModel;

@Repository
public interface VehicleRepository extends JpaRepository<VehicleModel, Integer> {
    Optional<VehicleModel> findByPlate(String plate);
    List<VehicleModel> findByIdCustomer(Integer idCustomer);
    List<VehicleModel> findByIdCustomerAndMonthlyVehicle(Integer idCustomer, Boolean monthlyVehicle);
    Page<VehicleModel> findByIdCustomerAndMonthlyVehicle(Integer idCustomer, Boolean monthlyVehicle, Pageable pageable);
}
