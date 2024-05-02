package com.sys.park.app.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sys.park.app.models.TicketModel;

@Repository
public interface TicketRepository extends JpaRepository<TicketModel, Integer> {
    Optional<TicketModel> findByIdCustomerVehicle(Integer idCustomerVehicle);
    List<TicketModel> findByIsActive(Boolean isActive);
    Optional<TicketModel> findByIdCustomerVehicleAndIsActive(Integer idCustomerVehicle, Boolean isActive);

}
