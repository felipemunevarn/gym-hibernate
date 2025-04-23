package entity;

import com.epam.entity.Trainer;
import com.epam.entity.Training;
import com.epam.entity.TrainingType;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
import com.epam.entity.Trainee;

class TrainingTest {

    @Test
    void testBuilderAndGetters() {
        Trainee trainee = Trainee.builder().build(); // minimal mock objects
        Trainer trainer = Trainer.builder().build();
        TrainingType trainingType = new TrainingType();
        LocalDate date = LocalDate.of(2025, 4, 22);

        Training training = Training.builder()
                .id(1L)
                .trainee(trainee)
                .trainer(trainer)
                .name("Morning Cardio")
                .trainingType(trainingType)
                .date(date)
                .duration(60)
                .build();

        assertEquals(1L, training.getId());
        assertSame(trainee, training.getTrainee());
        assertSame(trainer, training.getTrainer());
        assertEquals("Morning Cardio", training.getName());
        assertSame(trainingType, training.getTrainingType());
        assertEquals(date, training.getDate());
        assertEquals(60, training.getDuration());
    }

    @Test
    void testToBuilder() {
        Trainee trainee = Trainee.builder().build();
        Trainer trainer = Trainer.builder().build();
        TrainingType trainingType = new TrainingType();
        LocalDate date = LocalDate.now();

        Training original = Training.builder()
                .id(42L)
                .trainee(trainee)
                .trainer(trainer)
                .name("Session A")
                .trainingType(trainingType)
                .date(date)
                .duration(30)
                .build();

        Training copy = original.toBuilder().build();

        assertEquals(original.getId(), copy.getId());
        assertEquals(original.getTrainee(), copy.getTrainee());
        assertEquals(original.getTrainer(), copy.getTrainer());
        assertEquals(original.getName(), copy.getName());
        assertEquals(original.getTrainingType(), copy.getTrainingType());
        assertEquals(original.getDate(), copy.getDate());
        assertEquals(original.getDuration(), copy.getDuration());
    }

    @Test
    void testNoArgsConstructor() {
        Training training = Training.builder().build();
        assertNotNull(training);
    }
}

