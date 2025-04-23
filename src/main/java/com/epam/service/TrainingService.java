package com.epam.service;

import com.epam.entity.*;
import com.epam.repository.TraineeRepository;
import com.epam.repository.TrainerRepository;
import com.epam.repository.TrainingRepository;
import com.epam.repository.TrainingTypeRepository;
import com.epam.util.UsernamePasswordUtil;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TrainingService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final TrainingRepository trainingRepository;
    private final UserService userService;
//    private final UsernamePasswordUtil usernamePasswordUtil;

    @Autowired
    public TrainingService(TraineeRepository traineeRepository,
                           TrainerRepository trainerRepository,
                           TrainingRepository trainingRepository,
                           UserService userService/*,
                           UsernamePasswordUtil usernamePasswordUtil*/) {
        this.traineeRepository = traineeRepository;
        this.trainerRepository = trainerRepository;
        this.trainingRepository = trainingRepository;
        this.userService = userService;
//        this.usernamePasswordUtil = usernamePasswordUtil;
    }

    @Transactional
    public void createTraining(String username,
                               String password,
                               String traineeUsername,
                               String trainerUsername,
                               String name,
                               TrainingType trainingType,
                               LocalDate date,
                               int duration) {
        if (!authenticate(username, password)) {
            log.warn("Authentication failed for {}", username);
        }
        log.info("Authentication successful for {}", username);

        Optional<Trainee> optTrainee = traineeRepository.findByUserUsername(traineeUsername);
        Optional<Trainer> optTrainer = trainerRepository.findByUserUsername(trainerUsername);

        if (!optTrainee.isPresent() || !optTrainer.isPresent()) {
            throw new NoResultException();
        }

        Training training = new Training.Builder()
                .trainee(optTrainee.get())
                .trainer(optTrainer.get())
                .name(name)
                .trainingType(trainingType)
                .date(date)
                .duration(duration)
                .build();

        log.info("Creating training: {}", training.getName());
        try {
            trainingRepository.save(training);
            log.debug("Training saved with ID: {}", training.getId());
        } catch (Exception e) {
            log.error("Failed to save trainer: {}", e.getMessage(), e);
        }
    }

    @Transactional
    public boolean authenticate(String username, String password) {
        return userService.authenticate(username, password);
    }

    @Transactional
    public List<Training> getTraineeTrainings(String username, LocalDate from, LocalDate to, String trainerName, TrainingTypeEnum type) {
        return trainingRepository.findTraineeTrainingsByCriteria(username, from, to, trainerName, type);
    }

    @Transactional
    public List<Training> getTrainerTrainings(String username, LocalDate from, LocalDate to, String traineeName) {
        return trainingRepository.findTrainerTrainingsByCriteria(username, from, to, traineeName);
    }

//
//    @Transactional
//    public Optional<Trainer> findByUsername(String username) {
//        return trainerRepository.findByUserUsername(username);
//    }
//
//    @Transactional
//    public Optional<Trainer> getAuthenticatedTrainer(String username, String password) {
//        if (!authenticate(username, password)) {
//            log.warn("Authentication failed for {}", username);
//            return Optional.empty();
//        }
//        log.info("Authentication successful for {}", username);
//        return findByUsername(username);
//    }

}
