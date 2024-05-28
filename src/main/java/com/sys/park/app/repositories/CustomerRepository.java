package com.sys.park.app.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sys.park.app.models.CustomerModel;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerModel, Integer> {
    Page<CustomerModel> findByIdCustomerTypeAndIsActive(Integer idCustomerType, Boolean isActive, Pageable page);
    Optional<CustomerModel> findByIdPerson(Integer idPerson);
    List<CustomerModel> findByIdCustomerTypeAndIsActive(Integer idCustomerType, Boolean isActive);
    Boolean existsByIdPerson(Integer IdPerson);
}
