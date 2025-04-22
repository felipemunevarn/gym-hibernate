package com.epam.repository;

import com.epam.entity.TrainingType;
import com.epam.entity.TrainingTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrainingTypeRepository extends JpaRepository<TrainingType, Long> {
    boolean existsByType(TrainingTypeEnum type);
    Optional<TrainingType> findByType(TrainingTypeEnum trainingTypeEnum);
}
