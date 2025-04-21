package com.epam;

import com.epam.config.AppConfig;
import com.epam.entity.User;
import com.epam.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        UserService userService = context.getBean(UserService.class);
        User user;
        try {
            user = new User.Builder()
                    .firstName("John")
                    .lastName("Doe")
                    .username("john.doe")
                    .password("abcd1234")
                    .isActive(true)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        userService.createUser(user);
        context.close();
    }
}