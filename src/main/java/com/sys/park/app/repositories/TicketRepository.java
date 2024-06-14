package com.sys.park.app.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sys.park.app.models.TicketModel;

@Repository
public interface TicketRepository extends JpaRepository<TicketModel, Integer> {
    List<TicketModel> findByIdCustomerVehicle(Integer idCustomerVehicle);
    Page<TicketModel> findByIsActive(Boolean isActive, Pageable page);
    List<TicketModel> findByIsActive(Boolean isActive);
    Optional<TicketModel> findByIdCustomerVehicleAndIsActive(Integer idCustomerVehicle, Boolean isActive);

    @Query(value = "SELECT COUNT(*) > 0 " +
                   "FROM ticket t " +
                   "JOIN vehicle_customer vc ON t.id_customer_vehicle = vc.id " +
                   "JOIN vehicle v ON vc.id_vehicle = v.id " +
                   "WHERE v.plate = :plate AND t.is_active = true", nativeQuery = true)
    boolean existsActiveTicketByVehiclePlate(String plate);
}
