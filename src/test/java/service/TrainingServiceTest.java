package service;

import com.epam.entity.*;
import com.epam.repository.TraineeRepository;
import com.epam.repository.TrainerRepository;
import com.epam.repository.TrainingRepository;
import com.epam.service.TrainingService;
import com.epam.service.UserService;
import jakarta.persistence.NoResultException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.slf4j.Logger;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainingServiceTest {

    @Mock private TraineeRepository traineeRepository;
    @Mock private TrainerRepository trainerRepository;
    @Mock private TrainingRepository trainingRepository;
    @Mock private UserService userService;

    @InjectMocks private TrainingService trainingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTraining_Success() {
        Trainee trainee = new Trainee.Builder().user(new User.Builder().username("trainee").build()).build();
        Trainer trainer = new Trainer.Builder().user(new User.Builder().username("trainer").build()).build();

        when(userService.authenticate("admin", "adminpass")).thenReturn(true);
        when(traineeRepository.findByUserUsername("trainee")).thenReturn(Optional.of(trainee));
        when(trainerRepository.findByUserUsername("trainer")).thenReturn(Optional.of(trainer));

        trainingService.createTraining(
                "admin",
                "adminpass",
                "trainee",
                "trainer",
                "Morning Cardio",
                new TrainingType(TrainingTypeEnum.CARDIO),
                LocalDate.now(),
                60
        );

        verify(trainingRepository).save(any(Training.class));
    }

    @Test
    void testCreateTraining_Failure_MissingTrainee() {
        when(userService.authenticate("admin", "adminpass")).thenReturn(true);
        when(traineeRepository.findByUserUsername("trainee")).thenReturn(Optional.empty());

        assertThrows(NoResultException.class, () ->
                trainingService.createTraining("admin", "adminpass", "trainee", "trainer", "Training", new TrainingType(TrainingTypeEnum.CARDIO), LocalDate.now(), 60)
        );
    }

    @Test
    void testAuthenticate() {
        when(userService.authenticate("user", "pass")).thenReturn(true);
        assertTrue(trainingService.authenticate("user", "pass"));
    }

    @Test
    void testGetTraineeTrainings() {
        when(trainingRepository.findTraineeTrainingsByCriteria("trainee", null, null, null, null))
                .thenReturn(Collections.emptyList());

        assertNotNull(trainingService.getTraineeTrainings("trainee", null, null, null, null));
    }

    @Test
    void testGetTrainerTrainings() {
        when(trainingRepository.findTrainerTrainingsByCriteria("trainer", null, null, null))
                .thenReturn(Collections.emptyList());

        assertNotNull(trainingService.getTrainerTrainings("trainer", null, null, null));
    }
}

