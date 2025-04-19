package com.sys.park.app.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sys.park.app.models.CustomerTypeModel;

@Repository
public interface CustomerTypeRepository extends JpaRepository<CustomerTypeModel, Long> {
    Optional<CustomerTypeModel> findByName(String name);
}
