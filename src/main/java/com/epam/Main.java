package com.epam;

import com.epam.config.AppConfig;
import com.epam.entity.Trainee;
import com.epam.entity.User;
import com.epam.service.TraineeService;
import com.epam.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        TraineeService traineeService = context.getBean(TraineeService.class);
//        Trainee trainee;
        traineeService.createTrainee("John", "Doe", LocalDate.of(2000,1,2), "123 Elm Street");
//        System.out.println("Trainee verified: " + traineeService.authenticate("john.doe3", "jxwm5mAaud"));
//        System.out.println("Trainee verified: " + traineeService.authenticate("john.doe3", "1234abcd"));
//        System.out.println("Trainee: " + traineeService.findByUsername("john.doe3"));
//        traineeService.changeTraineePassword("john.doe","1234", "5678");
//        traineeService.changeTraineePassword("john.doe3","jxwm5mAaud", "5678abcd");
//        traineeService.changeTraineePassword("john.doe3","5678abcd", "jxwm5mAaud");
//        traineeService.updateTrainee("john.doe3", LocalDate.of(1999,11,12), "123 Siempre Viva St");
//        traineeService.changeActiveStatus("john.doe3", false);
//        traineeService.changeActiveStatus("john.doe3", true);
//        traineeService.deleteTrainee("john.doe3");
        context.close();
    }
}