package com.sys.park.app.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sys.park.app.models.VehicleModel;

@Repository
public interface VehicleRepository extends JpaRepository<VehicleModel, Integer> {
    Optional<VehicleModel> findByPlate(String plate);
}
