package com.sys.park.app.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sys.park.app.models.TicketModel;

@Repository
public interface TicketRepository extends JpaRepository<TicketModel, Integer> {
    Optional<TicketModel> findByIdVehicle(Integer idVehicle);
    Page<TicketModel> findByIsActive(Boolean isActive, Pageable pageable);
    Optional<TicketModel> findByIdVehicleAndIsActive(Integer idVehicle, Boolean isActive);
    List<TicketModel> findAllByOrderByIdAsc(Pageable pageable);
}
