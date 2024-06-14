package com.sys.park.app.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import com.sys.park.app.models.UserModel;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Integer>{
    //Page<UserModel> findByIsActive(Boolean isActive, Pageable page);
    //Optional<UserModel> findByUsername(String username);
    Optional<UserModel> findByIdPerson(Integer idPerson);
    UserDetails findByUsername(String username);
    //Boolean existsByIdPerson(Integer IdPerson);
    Boolean existsByUsername(String username);
}
