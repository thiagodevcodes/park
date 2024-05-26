package com.sys.park.app.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sys.park.app.models.UserTypeModel;

@Repository
public interface UserTypeRepository extends JpaRepository<UserTypeModel, Integer> {
    Optional<UserTypeModel> findByName(String name);
}
