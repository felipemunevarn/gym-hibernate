package com.epam.service;

import com.epam.entity.Trainee;
import com.epam.entity.Training;
import com.epam.entity.User;
import com.epam.repository.TraineeRepository;
import com.epam.repository.TrainingRepository;
import com.epam.repository.UserRepository;
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
public class TraineeService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final TraineeRepository traineeRepository;
    private final TrainingRepository trainingRepository;
    private final TrainingService trainingService;
    private final UserService userService;
    private final UsernamePasswordUtil usernamePasswordUtil;

    @Autowired
    public TraineeService(TraineeRepository traineeRepository,
                          TrainingRepository trainingRepository,
                          TrainingService trainingService,
                          UserService userService,
                          UsernamePasswordUtil usernamePasswordUtil) {
        this.traineeRepository = traineeRepository;
        this.trainingRepository = trainingRepository;
        this.trainingService = trainingService;
        this.userService = userService;
        this.usernamePasswordUtil = usernamePasswordUtil;
    }

    @Transactional
    public void createTrainee(String firstName, String lastName, LocalDate dateOfBirth, String address) {

        String newUsername = usernamePasswordUtil.generateUsername(firstName, lastName);
        String newPassword = usernamePasswordUtil.generatePassword();
        User user = new User.Builder()
                .firstName(firstName)
                .lastName(lastName)
                .username(newUsername)
                .password(newPassword)
                .isActive(true)
                .build();

        Trainee trainee = new Trainee.Builder()
                .dateOfBirth(dateOfBirth)
                .address(address)
                .user(user)
                .build();

        log.info("Creating trainee: {}", user.getUsername());
        try {
            traineeRepository.save(trainee);
            log.debug("Trainee saved with ID: {}", trainee.getId());
        } catch (Exception e) {
            log.error("Failed to save trainee: {}", e.getMessage(), e);
        }
    }

    @Transactional
    public boolean authenticate(String username, String password) {
        return userService.authenticate(username, password);
    }

    @Transactional
    public Optional<Trainee> findByUsername(String username) {
        log.debug("Finding trainee by username: {}", username);
        Optional<Trainee> optionalTrainee = traineeRepository.findByUserUsername(username);
        if (optionalTrainee.isEmpty()) {
            log.error("Trainee not found with username: {}", username);
            throw new NoResultException("Trainee not found");
        }
        return optionalTrainee;
    }

    @Transactional
    public void changeTraineePassword(String username, String oldPassword, String newPassword) {
        Optional<Trainee> optionalTrainee = findByUsername(username);
        userService.changePassword(username, oldPassword, newPassword);
    }

    @Transactional
    public void updateTrainee(String username, LocalDate dateOfBirth, String address) {
        Optional<Trainee> optTrainee = findByUsername(username);

        Trainee trainee = optTrainee.get().toBuilder()
                .dateOfBirth(dateOfBirth)
                .address(address)
                .build();
        traineeRepository.save(trainee);
        log.info("Trainee with username: {} updated succesfully!", username);
    }

    @Transactional
    public void changeActiveStatus(String username, boolean isActive) {
        Optional<Trainee> optTrainee = findByUsername(username);
        userService.setActiveStatus(username, isActive);
    }

    @Transactional
    public void deleteTrainee(String username) {
        List<Training> trainings = trainingService.getTraineeTrainings(username,null,null,null,null);
        for (Training t : trainings) {
            trainingRepository.delete(t);
        }
        Optional<Trainee> optTrainee = findByUsername(username);
        traineeRepository.deleteByUserUsername(username);
        log.info("Trainee deleted with username: {}", username);
    }
}
