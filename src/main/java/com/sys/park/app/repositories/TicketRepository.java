package com.sys.park.app.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sys.park.app.models.TicketModel;

@Repository
public interface TicketRepository extends JpaRepository<TicketModel, Integer> {
    List<TicketModel> findByIdCustomerVehicle(Integer idCustomerVehicle);
    List<TicketModel> findByRegisterDateAndIsActive(LocalDate localDate, Boolean isActive);
    Page<TicketModel> findByIsActive(Boolean isActive, Pageable page);
    List<TicketModel> findByIsActive(Boolean isActive);
    Optional<TicketModel> findByIdCustomerVehicleAndIsActive(Integer idCustomerVehicle, Boolean isActive);
}
