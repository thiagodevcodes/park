package com.sys.park.app.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sys.park.app.models.CustomerModel;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerModel, Integer> {
    List<CustomerModel> findByIdCustomerTypeAndIsActive(Integer idCustomerType, Boolean isActive);
    Optional<CustomerModel> findByIdPerson(Integer idPerson);
}
