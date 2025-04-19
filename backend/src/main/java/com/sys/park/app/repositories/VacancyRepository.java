package com.sys.park.app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sys.park.app.models.VacancyModel;

@Repository
public interface VacancyRepository extends JpaRepository<VacancyModel, Long> {
    Integer countBySituationTrue();
    Integer countBySituationFalse();
    List<VacancyModel> findBySituation(Boolean situation);
}
