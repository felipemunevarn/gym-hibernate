package com.epam.bootstrap;

import com.epam.entity.TrainingType;
import com.epam.entity.TrainingTypeEnum;
import com.epam.repository.TrainingTypeRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class TrainingTypeSeeder {

    private final TrainingTypeRepository trainingTypeRepository;

    public TrainingTypeSeeder(TrainingTypeRepository trainingTypeRepository) {
        this.trainingTypeRepository = trainingTypeRepository;
    }

    @PostConstruct
    public void seedTrainingTypes() {
        for (TrainingTypeEnum typeEnum : TrainingTypeEnum.values()) {
            if (!trainingTypeRepository.existsByType(typeEnum)) {
                trainingTypeRepository.save(new TrainingType(typeEnum));
            }
        }
    }
}

