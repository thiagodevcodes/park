package com.sys.park.app.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sys.park.app.models.CustomerModel;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerModel, Long> {
    Page<CustomerModel> findByIdCustomerTypeAndIsActive(Integer idCustomerType, Boolean isActive, Pageable page);
    Optional<CustomerModel> findByIdPerson(Long long1);
    List<CustomerModel> findByIdCustomerTypeAndIsActive(Integer idCustomerType, Boolean isActive);
    Boolean existsByIdPerson(Long long1);
}
