package com.sys.park.app.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sys.park.app.models.PersonModel;

public interface PersonRepository extends JpaRepository<PersonModel, Long> {
    Optional<PersonModel> findByCpf(String cpf);
    Optional<PersonModel> findByEmail(String email);
    Optional<PersonModel> findByPhone(String phone);
    Boolean existsByEmail(String email);
    Boolean existsByCpf(String cpf);
    Boolean existsByPhone(String phone);
}
