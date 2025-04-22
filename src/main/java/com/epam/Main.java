package com.epam;

import com.epam.config.AppConfig;
import com.epam.entity.Trainee;
import com.epam.entity.TrainingType;
import com.epam.entity.TrainingTypeEnum;
import com.epam.entity.User;
import com.epam.repository.TrainingTypeRepository;
import com.epam.service.TraineeService;
import com.epam.service.TrainerService;
import com.epam.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        TraineeService traineeService = context.getBean(TraineeService.class);
        TrainerService trainerService = context.getBean(TrainerService.class);
        TrainingTypeRepository trainingTypeRepository = context.getBean(TrainingTypeRepository.class);
//        Trainee trainee;
//        traineeService.createTrainee("John", "Doe", LocalDate.of(2000,1,2), "123 Elm Street");
        TrainingType trainingType = trainingTypeRepository.findByType(TrainingTypeEnum.CARDIO)
                .orElseThrow(() -> new IllegalStateException("TrainingType not found"));
        TrainingType newTrainingType = trainingTypeRepository.findByType(TrainingTypeEnum.STRENGTH)
                .orElseThrow(() -> new IllegalStateException("TrainingType not found"));
//        trainerService.createTrainer("John", "Doe", trainingType);
//        System.out.println("Trainee verified: " + traineeService.authenticate("john.doe3", "jxwm5mAaud"));
//        System.out.println("Trainee verified: " + traineeService.authenticate("john.doe3", "1234abcd"));
//        System.out.println("Trainee: " + traineeService.findByUsername("john.doe3"));
//        System.out.println("Trainer: " + trainerService.getAuthenticatedTrainer("john.doe5","VFYA63Qd28"));
//        traineeService.changeTraineePassword("john.doe","1234", "5678");
//        traineeService.changeTraineePassword("john.doe3","jxwm5mAaud", "5678abcd");
//        traineeService.changeTraineePassword("john.doe3","5678abcd", "jxwm5mAaud");
//        trainerService.changeTrainerPassword("john.doe5","VFYA63Qd28", "5678abcd");
//        trainerService.changeTrainerPassword("john.doe5","5678abcd", "VFYA63Qd28");
//        traineeService.updateTrainee("john.doe3", LocalDate.of(1999,11,12), "123 Siempre Viva St");
//        trainerService.updateTrainer("john.doe5", "VFYA63Qd28", newTrainingType);
//        traineeService.changeActiveStatus("john.doe3", false);
//        traineeService.changeActiveStatus("john.doe3", true);
//        trainerService.changeActiveStatus("john.doe5", "VFYA63Qd28", false);
//        trainerService.changeActiveStatus("john.doe5", "VFYA63Qd28", true);
//        traineeService.deleteTrainee("john.doe3");
        context.close();
    }
}