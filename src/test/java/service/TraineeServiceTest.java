package service;

import com.epam.entity.Trainee;
import com.epam.entity.User;
import com.epam.repository.TraineeRepository;
import com.epam.repository.UserRepository;
import com.epam.service.TraineeService;
import com.epam.service.UserService;
import com.epam.util.UsernamePasswordUtil;
import jakarta.persistence.NoResultException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TraineeServiceTest {

    @Mock
    private TraineeRepository traineeRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserService userService;
    @Mock
    private UsernamePasswordUtil usernamePasswordUtil;

    @InjectMocks
    private TraineeService traineeService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTrainee() {
        String firstName = "John";
        String lastName = "Doe";
        LocalDate dob = LocalDate.of(2000, 1, 1);
        String address = "123 Street";

        when(usernamePasswordUtil.generateUsername(firstName, lastName)).thenReturn("john.doe");
        when(usernamePasswordUtil.generatePassword()).thenReturn("pass123");

        traineeService.createTrainee(firstName, lastName, dob, address);

        verify(traineeRepository, times(1)).save(any(Trainee.class));
    }

    @Test
    void testAuthenticate() {
        when(userService.authenticate("john", "pass")).thenReturn(true);
        assertTrue(traineeService.authenticate("john", "pass"));
    }

    @Test
    void testFindByUsernameFound() {
        User user = new User.Builder().username("john").isActive(true).build();
        Trainee trainee = new Trainee.Builder().user(user).build();
        when(traineeRepository.findByUserUsername("john")).thenReturn(Optional.of(trainee));

        Optional<Trainee> result = traineeService.findByUsername("john");
        assertTrue(result.isPresent());
        assertEquals("john", result.get().getUser().getUsername());
    }

    @Test
    void testFindByUsernameNotFound() {
        when(traineeRepository.findByUserUsername("unknown")).thenReturn(Optional.empty());
        assertThrows(NoResultException.class, () -> traineeService.findByUsername("unknown"));
    }

    @Test
    void testChangeTraineePassword() {
        User user = new User.Builder().username("john").build();
        Trainee trainee = new Trainee.Builder().user(user).build();
        when(traineeRepository.findByUserUsername("john")).thenReturn(Optional.of(trainee));

        traineeService.changeTraineePassword("john", "old", "new");
        verify(userService).changePassword("john", "old", "new");
    }

    @Test
    void testUpdateTrainee() {
        LocalDate newDob = LocalDate.of(1995, 5, 5);
        String newAddress = "New Address";
        User user = new User.Builder().username("john").build();

        Trainee trainee = new Trainee.Builder().user(user).dateOfBirth(LocalDate.of(2000, 1, 1)).address("Old").build();
        when(traineeRepository.findByUserUsername("john")).thenReturn(Optional.of(trainee));

        traineeService.updateTrainee("john", newDob, newAddress);
        verify(traineeRepository).save(argThat(t ->
                t.getDateOfBirth().equals(newDob) && t.getAddress().equals(newAddress)));
    }

    @Test
    void testChangeActiveStatus() {
        User user = new User.Builder().username("john").build();
        Trainee trainee = new Trainee.Builder().user(user).build();
        when(traineeRepository.findByUserUsername("john")).thenReturn(Optional.of(trainee));

        traineeService.changeActiveStatus("john", false);
        verify(userService).setActiveStatus("john", false);
    }

    @Test
    void testDeleteTrainee() {
        User user = new User.Builder().username("john").build();
        Trainee trainee = new Trainee.Builder().user(user).build();
        when(traineeRepository.findByUserUsername("john")).thenReturn(Optional.of(trainee));

        traineeService.deleteTrainee("john");
        verify(traineeRepository).deleteByUserUsername("john");
    }
}
