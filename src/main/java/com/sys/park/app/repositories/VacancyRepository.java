package com.sys.park.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sys.park.app.models.VacancyModel;

@Repository
public interface VacancyRepository extends JpaRepository<VacancyModel, Integer> {
    Integer countBySituationTrue();
    Integer countBySituationFalse();
}
