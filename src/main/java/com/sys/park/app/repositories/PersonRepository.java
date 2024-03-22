package com.sys.park.app.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sys.park.app.models.PersonModel;

public interface PersonRepository extends JpaRepository<PersonModel, Integer> {
    Optional<PersonModel> findByName(String name);
}
