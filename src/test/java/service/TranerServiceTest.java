package service;

import com.epam.entity.Trainer;
import com.epam.entity.TrainingType;
import com.epam.entity.TrainingTypeEnum;
import com.epam.entity.User;
import com.epam.repository.TrainerRepository;
import com.epam.repository.TrainingTypeRepository;
import com.epam.repository.UserRepository;
import com.epam.service.TrainerService;
import com.epam.service.UserService;
import com.epam.util.UsernamePasswordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainerServiceTest {

    @Mock
    private TrainerRepository trainerRepository;
    @Mock
    private TrainingTypeRepository trainingTypeRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserService userService;
    @Mock
    private UsernamePasswordUtil usernamePasswordUtil;

    @InjectMocks
    private TrainerService trainerService;

    private final TrainingType trainingType = trainingTypeRepository.findByType(TrainingTypeEnum.CARDIO)
            .orElseThrow(() -> new IllegalStateException("TrainingType not found"));
    private final User user = new User.Builder()
            .firstName("John")
            .lastName("Doe")
            .username("john.doe")
            .password("password123")
            .isActive(true)
            .build();
    private final Trainer trainer = new Trainer.Builder()
            .user(user)
            .trainingType(trainingType)
            .build();

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTrainer() {
        when(usernamePasswordUtil.generateUsername("John", "Doe")).thenReturn("john.doe");
        when(usernamePasswordUtil.generatePassword()).thenReturn("password123");

        trainerService.createTrainer("John", "Doe", trainingType);

        verify(trainerRepository).save(any(Trainer.class));
    }

    @Test
    void testAuthenticate_Success() {
        when(userService.authenticate("john.doe", "password123")).thenReturn(true);
        assertTrue(trainerService.authenticate("john.doe", "password123"));
    }

    @Test
    void testFindByUsername() {
        when(trainerRepository.findByUserUsername("john.doe")).thenReturn(Optional.of(trainer));

        Optional<Trainer> found = trainerService.findByUsername("john.doe");

        assertTrue(found.isPresent());
        assertEquals("john.doe", found.get().getUser().getUsername());
    }

    @Test
    void testGetAuthenticatedTrainer_Success() {
        when(userService.authenticate("john.doe", "password123")).thenReturn(true);
        when(trainerRepository.findByUserUsername("john.doe")).thenReturn(Optional.of(trainer));

        Optional<Trainer> result = trainerService.getAuthenticatedTrainer("john.doe", "password123");

        assertTrue(result.isPresent());
    }

    @Test
    void testGetAuthenticatedTrainer_Failure() {
        when(userService.authenticate("john.doe", "wrongpass")).thenReturn(false);

        Optional<Trainer> result = trainerService.getAuthenticatedTrainer("john.doe", "wrongpass");

        assertTrue(result.isEmpty());
    }

    @Test
    void testChangeTrainerPassword_Success() {
        when(trainerRepository.findByUserUsername("john.doe")).thenReturn(Optional.of(trainer));
        when(userService.authenticate("john.doe", "oldpass")).thenReturn(true);

        trainerService.changeTrainerPassword("john.doe", "oldpass", "newpass");

        verify(userService).changePassword("john.doe", "oldpass", "newpass");
    }

    @Test
    void testUpdateTrainer_Success() {
        TrainingType newType = trainingTypeRepository.findByType(TrainingTypeEnum.CARDIO)
                .orElseThrow(() -> new IllegalStateException("TrainingType not found"));
        when(trainerRepository.findByUserUsername("john.doe")).thenReturn(Optional.of(trainer));
        when(userService.authenticate("john.doe", "password123")).thenReturn(true);

        trainerService.updateTrainer("john.doe", "password123", newType);

        verify(trainerRepository).save(any(Trainer.class));
    }

    @Test
    void testChangeActiveStatus_Success() {
        when(trainerRepository.findByUserUsername("john.doe")).thenReturn(Optional.of(trainer));
        when(userService.authenticate("john.doe", "password123")).thenReturn(true);

        trainerService.changeActiveStatus("john.doe", "password123", false);

        verify(userService).setActiveStatus("john.doe", false);
    }
}

