package com.sys.park.app.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sys.park.app.models.UserModel;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Integer>{
    Optional<UserModel> findByLogin(String login);
}
