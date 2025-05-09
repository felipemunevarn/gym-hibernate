package com.epam;

import com.epam.config.AppConfig;
import com.epam.entity.*;
import com.epam.repository.TrainingTypeRepository;
import com.epam.service.TraineeService;
import com.epam.service.TrainerService;
import com.epam.service.TrainingService;
import com.epam.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        TraineeService traineeService = context.getBean(TraineeService.class);
        TrainerService trainerService = context.getBean(TrainerService.class);
        TrainingService trainingService = context.getBean(TrainingService.class);
        TrainingTypeRepository trainingTypeRepository = context.getBean(TrainingTypeRepository.class);
//        Trainee trainee;
//        traineeService.createTrainee("John", "Doe", LocalDate.of(2000,1,2), "123 Elm Street");
        TrainingType trainingType = trainingTypeRepository.findByType(TrainingTypeEnum.CARDIO)
                .orElseThrow(() -> new IllegalStateException("TrainingType not found"));
        TrainingType newTrainingType = trainingTypeRepository.findByType(TrainingTypeEnum.STRENGTH)
                .orElseThrow(() -> new IllegalStateException("TrainingType not found"));
//        trainerService.createTrainer("Carlos", "Valderrama", newTrainingType);
//        trainingService.createTraining("john.doe4",
//                "yunTycejIJ",
//                "john.doe4",
//                "john.doe5",
//                "morning yoga",
//                trainingType,
//                LocalDate.of(2025,4,20),
//                45);
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
//        List<Trainer> trainers = trainerService.getUnassignedTrainersForTrainee("john.doe4");
//        System.out.println(trainers.get(0).getUser().getLastName());
//        traineeService.deleteTrainee("john.doe3");
//        List<Training> trainings = trainingService.getTraineeTrainings("john.doe4",null, null, "William", null);
//        System.out.println(trainings);
//        System.out.println(trainings.getLast().getTrainer());
        context.close();
    }
}