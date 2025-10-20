package com.shyn9yskhan.trainer_service.service;

import com.shyn9yskhan.trainer_service.dto.*;
import com.shyn9yskhan.trainer_service.entity.TrainerEntity;
import com.shyn9yskhan.trainer_service.repository.TrainerRepository;
import com.shyn9yskhan.trainer_service.service.exception.TrainerCreationException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerServiceImplTest {

    @Mock
    private TrainerRepository trainerRepository;

    @InjectMocks
    private TrainerServiceImpl trainerService;

    private String sampleUserId;
    private String sampleTrainerId;

    @BeforeEach
    void setUp() {
        sampleUserId = "user-" + UUID.randomUUID();
        sampleTrainerId = "trainer-" + UUID.randomUUID();
    }

    @Test
    void createTrainer_success() {
        CreateTrainerRequest req = new CreateTrainerRequest("spec-x", sampleUserId);

        TrainerEntity saved = new TrainerEntity(sampleTrainerId, "spec-x", sampleUserId);
        when(trainerRepository.save(any(TrainerEntity.class))).thenReturn(saved);

        CreateTrainerResponse resp = trainerService.createTrainer(req);

        assertThat(resp).isNotNull();
        assertThat(resp.id()).isEqualTo(sampleTrainerId);
        assertThat(resp.specialization()).isEqualTo("spec-x");

        ArgumentCaptor<TrainerEntity> captor = ArgumentCaptor.forClass(TrainerEntity.class);
        verify(trainerRepository).save(captor.capture());
        TrainerEntity toSave = captor.getValue();
        assertThat(toSave.getSpecialization()).isEqualTo("spec-x");
        assertThat(toSave.getUserId()).isEqualTo(sampleUserId);
    }

    @Test
    void createTrainer_whenSaveThrowsDataIntegrity_thenTrainerCreationException() {
        CreateTrainerRequest req = new CreateTrainerRequest("spec-x", sampleUserId);
        when(trainerRepository.save(any())).thenThrow(new DataIntegrityViolationException("dup"));

        assertThatThrownBy(() -> trainerService.createTrainer(req))
                .isInstanceOf(TrainerCreationException.class)
                .hasMessageContaining("Failed to create trainer");

        verify(trainerRepository).save(any());
    }

    @Test
    void getTrainer_success() {
        TrainerEntity entity = new TrainerEntity(sampleTrainerId, "spec-a", sampleUserId);
        when(trainerRepository.findById(sampleTrainerId)).thenReturn(Optional.of(entity));

        GetTrainerResponse resp = trainerService.getTrainer(sampleTrainerId);

        assertThat(resp).isNotNull();
        assertThat(resp.specialization()).isEqualTo("spec-a");
    }

    @Test
    void getTrainer_notFound_throws() {
        when(trainerRepository.findById("missing")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> trainerService.getTrainer("missing"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Trainer not found");
    }

    @Test
    void getTrainingTypeIdByTrainerId_success() {
        String trainingTypeId = "tt-123";
        when(trainerRepository.findTrainingTypeIdByTrainerId(sampleTrainerId)).thenReturn(Optional.of(trainingTypeId));

        GetTrainingTypeIdByTrainerIdResponse resp = trainerService.getTrainingTypeIdByTrainerId(sampleTrainerId);

        assertThat(resp).isNotNull();
        assertThat(resp.trainingTypeId()).isEqualTo(trainingTypeId);
    }

    @Test
    void getTrainingTypeIdByTrainerId_notFound_throws() {
        when(trainerRepository.findTrainingTypeIdByTrainerId("missing")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> trainerService.getTrainingTypeIdByTrainerId("missing"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Training type not found");
    }

    @Test
    void getTrainerIdByUserId_success() {
        String expectedTrainerId = "t-by-user";
        when(trainerRepository.findIdByUserId(sampleUserId)).thenReturn(Optional.of(expectedTrainerId));

        GetTrainerIdByUserIdResponse resp = trainerService.getTrainerIdByUserId(sampleUserId);

        assertThat(resp).isNotNull();
        assertThat(resp.trainerId()).isEqualTo(expectedTrainerId);
    }

    @Test
    void getTrainerIdByUserId_notFound_throws() {
        when(trainerRepository.findIdByUserId("no-user")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> trainerService.getTrainerIdByUserId("no-user"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Trainer not found");
    }

    @Test
    void getTrainersByIds_mapsEntitiesToDtos() {
        TrainerEntity e1 = new TrainerEntity("id-1", "spec1", "user-1");
        TrainerEntity e2 = new TrainerEntity("id-2", "spec2", "user-2");
        when(trainerRepository.findByIdIn(List.of("id-1","id-2"))).thenReturn(List.of(e1, e2));

        List<TrainerDto> dtos = trainerService.getTrainersByIds(List.of("id-1","id-2"));

        assertThat(dtos).hasSize(2);
        assertThat(dtos).extracting("id").containsExactlyInAnyOrder("id-1","id-2");
    }

    @Test
    void getTrainersByUserIds_mapsEntitiesToDtos() {
        TrainerEntity e1 = new TrainerEntity("id-A", "specA", "uA");
        TrainerEntity e2 = new TrainerEntity("id-B", "specB", "uB");
        when(trainerRepository.findByUserIdIn(List.of("uA","uB"))).thenReturn(List.of(e1, e2));

        List<TrainerDto> dtos = trainerService.getTrainersByUserIds(List.of("uA","uB"));

        assertThat(dtos).hasSize(2);
        assertThat(dtos).extracting("userId").containsExactlyInAnyOrder("uA","uB");
    }

    @Test
    void updateTrainer_success() {
        UpdateTrainerRequest upd = new UpdateTrainerRequest("new-spec");
        when(trainerRepository.updateTrainer(eq(sampleTrainerId), anyString())).thenReturn(1);

        TrainerEntity updated = new TrainerEntity(sampleTrainerId, "new-spec", sampleUserId);
        when(trainerRepository.findById(sampleTrainerId)).thenReturn(Optional.of(updated));

        UpdateTrainerResponse resp = trainerService.updateTrainer(sampleTrainerId, upd);

        assertThat(resp).isNotNull();
        assertThat(resp.specialization()).isEqualTo("new-spec");
    }

    @Test
    void updateTrainer_notFound_throws() {
        UpdateTrainerRequest upd = new UpdateTrainerRequest("new-spec");
        when(trainerRepository.updateTrainer(eq("nope"), anyString())).thenReturn(0);

        assertThatThrownBy(() -> trainerService.updateTrainer("nope", upd))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Trainer not found");
    }

    @Test
    void deleteTrainer_notFound_throws() {
        when(trainerRepository.deleteTrainerById("nope")).thenReturn(0);
        assertThatThrownBy(() -> trainerService.deleteTrainer("nope"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Trainer not found");
    }

    @Test
    void getTrainerByUserId_success() {
        TrainerEntity entity = new TrainerEntity(sampleTrainerId, "specX", sampleUserId);
        when(trainerRepository.findByUserId(sampleUserId)).thenReturn(Optional.of(entity));

        GetTrainerResponse resp = trainerService.getTrainerByUserId(sampleUserId);

        assertThat(resp).isNotNull();
        assertThat(resp.specialization()).isEqualTo("specX");
    }

    @Test
    void updateTrainerByUserId_success() {
        UpdateTrainerRequest upd = new UpdateTrainerRequest("sNew");
        when(trainerRepository.updateTrainerByUserId(eq(sampleUserId), anyString())).thenReturn(1);
        TrainerEntity entity = new TrainerEntity(sampleTrainerId, "sNew", sampleUserId);
        when(trainerRepository.findByUserId(sampleUserId)).thenReturn(Optional.of(entity));

        UpdateTrainerResponse resp = trainerService.updateTrainerByUserId(sampleUserId, upd);

        assertThat(resp).isNotNull();
        assertThat(resp.specialization()).isEqualTo("sNew");
    }

    @Test
    void updateTrainerByUserId_notFound_throws() {
        UpdateTrainerRequest upd = new UpdateTrainerRequest("sNew");
        when(trainerRepository.updateTrainerByUserId(eq("no-user"), anyString())).thenReturn(0);

        assertThatThrownBy(() -> trainerService.updateTrainerByUserId("no-user", upd))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Trainer not found");
    }

    @Test
    void deleteTrainerByUserId_notFound_throws() {
        when(trainerRepository.deleteTrainerByUserIdQuery("no-user")).thenReturn(0);
        assertThatThrownBy(() -> trainerService.deleteTrainerByUserId("no-user"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Trainer not found");
    }

    // Validation tests for create/update payloads
    @Test
    void createTrainer_validation_throwsForNullOrBlank() {
        assertThatThrownBy(() -> trainerService.createTrainer(null)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> trainerService.createTrainer(new CreateTrainerRequest(" ", sampleUserId))).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> trainerService.createTrainer(new CreateTrainerRequest("spec", " "))).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void updateTrainer_validation_throwsForNullOrBlank() {
        assertThatThrownBy(() -> trainerService.updateTrainer(sampleTrainerId, null)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> trainerService.updateTrainer(sampleTrainerId, new UpdateTrainerRequest(" "))).isInstanceOf(IllegalArgumentException.class);
    }
}
