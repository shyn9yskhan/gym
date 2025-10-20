package com.shyn9yskhan.training_service.service;

import com.shyn9yskhan.training_service.dto.*;
import com.shyn9yskhan.training_service.entity.TrainingEntity;
import com.shyn9yskhan.training_service.repository.TrainingRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingServiceImplTest {

    @Mock
    private TrainingRepository trainingRepository;

    @InjectMocks
    private TrainingServiceImpl trainingService;

    private final String traineeId = "trainee-1";
    private final String trainerId = "trainer-1";

    @Test
    void createTraining_success() {
        CreateTrainingRequest req = new CreateTrainingRequest(
                traineeId,
                trainerId,
                "Yoga session",
                "type-abc",
                LocalDate.of(2025, 9, 1),
                60
        );

        TrainingEntity saved = new TrainingEntity("generated-id", traineeId, trainerId, "Yoga session", "type-abc", LocalDate.of(2025, 9, 1), 60);
        when(trainingRepository.save(ArgumentMatchers.any(TrainingEntity.class))).thenReturn(saved);

        CreateTrainingResponse resp = trainingService.createTraining(req);

        assertThat(resp).isNotNull();
        assertThat(resp.id()).isEqualTo("generated-id");
        assertThat(resp.traineeId()).isEqualTo(traineeId);
        assertThat(resp.trainerId()).isEqualTo(trainerId);
        assertThat(resp.trainingName()).isEqualTo("Yoga session");
        assertThat(resp.duration()).isEqualTo(60);

        verify(trainingRepository).save(ArgumentMatchers.any(TrainingEntity.class));
    }

    @Test
    void createTraining_validationFails_whenNullRequest() {
        assertThatThrownBy(() -> trainingService.createTraining(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("CreateTrainingRequest cannot be null");
    }

    @Test
    void createTraining_validationFails_whenMissingFields() {
        CreateTrainingRequest req = new CreateTrainingRequest(null, trainerId, "name", "type", LocalDate.now(), 30);
        assertThatThrownBy(() -> trainingService.createTraining(req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Trainee ID is required");

        CreateTrainingRequest req2 = new CreateTrainingRequest(traineeId, trainerId, "name", "type", LocalDate.now(), 0);
        assertThatThrownBy(() -> trainingService.createTraining(req2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Duration must be positive");
    }

    @Test
    void getTraining_success() {
        String id = "training-123";
        TrainingEntity entity = new TrainingEntity(id, traineeId, trainerId, "Strength", "type-1", LocalDate.of(2025, 5, 10), 45);
        when(trainingRepository.findById(id)).thenReturn(Optional.of(entity));

        GetTrainingResponse resp = trainingService.getTraining(id);

        assertThat(resp).isNotNull();
        assertThat(resp.traineeId()).isEqualTo(traineeId);
        assertThat(resp.trainerId()).isEqualTo(trainerId);
        assertThat(resp.trainingName()).isEqualTo("Strength");
        assertThat(resp.duration()).isEqualTo(45);
    }

    @Test
    void getTraining_notFound_throws() {
        when(trainingRepository.findById("missing")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> trainingService.getTraining("missing"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Training not found with ID");
    }

    @Test
    void updateTraining_success() {
        String id = "training-777";
        TrainingEntity existing = new TrainingEntity(id, traineeId, trainerId, "Old", "old-type", LocalDate.of(2025, 1, 1), 30);
        when(trainingRepository.findById(id)).thenReturn(Optional.of(existing));
        when(trainingRepository.save(existing)).thenReturn(existing);

        UpdateTrainingRequest updateReq = new UpdateTrainingRequest(
                traineeId,
                trainerId,
                "Updated name",
                "new-type",
                LocalDate.of(2025, 2, 2),
                75
        );

        UpdateTrainingResponse resp = trainingService.updateTraining(id, updateReq);

        assertThat(resp).isNotNull();
        assertThat(resp.trainingName()).isEqualTo("Updated name");
        assertThat(resp.duration()).isEqualTo(75);

        verify(trainingRepository).findById(id);
        verify(trainingRepository).save(existing);
    }

    @Test
    void updateTraining_notFound_throws() {
        when(trainingRepository.findById("x")).thenReturn(Optional.empty());
        UpdateTrainingRequest updateReq = new UpdateTrainingRequest(traineeId, trainerId, "n", "t", LocalDate.now(), 30);
        assertThatThrownBy(() -> trainingService.updateTraining("x", updateReq))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Training not found with ID");
    }

    @Test
    void updateTraining_validationFails_whenInvalidRequest() {
        String id = "some";
        UpdateTrainingRequest invalid = new UpdateTrainingRequest(null, trainerId, "n", "t", LocalDate.now(), 30);
        assertThatThrownBy(() -> trainingService.updateTraining(id, invalid))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("UpdateTrainingRequest cannot be null");
    }

    @Test
    void deleteTraining_success() {
        String id = "to-delete";
        when(trainingRepository.existsById(id)).thenReturn(true);
        doNothing().when(trainingRepository).deleteById(id);

        trainingService.deleteTraining(id);

        verify(trainingRepository).existsById(id);
        verify(trainingRepository).deleteById(id);
    }

    @Test
    void deleteTraining_notFound_throws() {
        when(trainingRepository.existsById("no")).thenReturn(false);
        assertThatThrownBy(() -> trainingService.deleteTraining("no"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Training not found with ID");
    }

    @Test
    void getTrainingsByTraineeId_success() {
        TrainingEntity e1 = new TrainingEntity("id1", traineeId, "trainerA", "n", "t1", LocalDate.of(2025, 3, 3), 30);
        TrainingEntity e2 = new TrainingEntity("id2", traineeId, "trainerB", "m", "t2", LocalDate.of(2025, 4, 4), 60);
        when(trainingRepository.findByTraineeId(traineeId)).thenReturn(List.of(e1, e2));

        GetTraineeTrainingsResponse resp = trainingService.getTrainingsByTraineeId(traineeId);

        assertThat(resp).isNotNull();
        assertThat(resp.trainings()).hasSize(2);
        assertThat(resp.trainings().get(0).traineeId()).isEqualTo(traineeId);
    }

    @Test
    void getTrainingsByTraineeId_validationFails_whenBlank() {
        assertThatThrownBy(() -> trainingService.getTrainingsByTraineeId(" "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Trainee ID is required");
    }

    @Test
    void getTrainingsByTrainerId_success() {
        TrainingEntity e1 = new TrainingEntity("id1", "t1", trainerId, "n", "t1", LocalDate.of(2025, 3, 3), 30);
        when(trainingRepository.findByTrainerId(trainerId)).thenReturn(List.of(e1));

        GetTrainerTrainingsResponse resp = trainingService.getTrainingsByTrainerId(trainerId);

        assertThat(resp).isNotNull();
        assertThat(resp.trainings()).hasSize(1);
        assertThat(resp.trainings().get(0).trainerId()).isEqualTo(trainerId);
    }

    @Test
    void getTrainingsByTrainerId_validationFails_whenBlank() {
        assertThatThrownBy(() -> trainingService.getTrainingsByTrainerId(" "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Trainer ID is required");
    }
}
