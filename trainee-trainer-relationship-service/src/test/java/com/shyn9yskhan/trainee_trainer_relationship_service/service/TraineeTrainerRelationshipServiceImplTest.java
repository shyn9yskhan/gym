package com.shyn9yskhan.trainee_trainer_relationship_service.service;

import com.shyn9yskhan.trainee_trainer_relationship_service.entity.TraineeTrainerRelationshipEntity;
import com.shyn9yskhan.trainee_trainer_relationship_service.repository.TraineeTrainerRelationshipRepository;
import com.shyn9yskhan.trainee_trainer_relationship_service.service.exception.RelationshipCreationException;
import com.shyn9yskhan.trainee_trainer_relationship_service.service.exception.RelationshipNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Collections;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraineeTrainerRelationshipServiceImplTest {

    @Mock
    private TraineeTrainerRelationshipRepository repository;

    @InjectMocks
    private TraineeTrainerRelationshipServiceImpl service;

    private final String traineeId = "trainee-1";
    private final String trainerId = "trainer-1";

    @BeforeEach
    void setUp() {
        // nothing for now
    }

    @Test
    void associate_whenDoesNotExist_savesEntity() {
        when(repository.existsById_TraineeIdAndId_TrainerId(traineeId, trainerId)).thenReturn(false);

        service.associate(traineeId, trainerId);

        ArgumentCaptor<TraineeTrainerRelationshipEntity> captor = ArgumentCaptor.forClass(TraineeTrainerRelationshipEntity.class);
        verify(repository).save(captor.capture());
        TraineeTrainerRelationshipEntity saved = captor.getValue();
        assertThat(saved.getId().getTraineeId()).isEqualTo(traineeId);
        assertThat(saved.getId().getTrainerId()).isEqualTo(trainerId);
    }

    @Test
    void associate_whenAlreadyExists_doesNotSave() {
        when(repository.existsById_TraineeIdAndId_TrainerId(traineeId, trainerId)).thenReturn(true);

        service.associate(traineeId, trainerId);

        verify(repository, never()).save(any());
    }

    @Test
    void associate_invalidIds_throws() {
        assertThatThrownBy(() -> service.associate(null, trainerId))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> service.associate(" ", trainerId))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> service.associate(traineeId, ""))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void associateTraineeWithTrainers_happyPath_savesAll() {
        List<String> trainerIds = List.of("t1", "t2", "t2", "  t3  ", null, ""); // includes dup, whitespace, null, empty
        // simulate saveAll returns list; not necessary to stub but do it for clarity
        when(repository.saveAll(any())).thenReturn(Collections.emptyList());

        service.associateTraineeWithTrainers(traineeId, trainerIds);

        // Expect saveAll called with 3 unique trimmed trainer ids: t1, t2, t3
        ArgumentCaptor<List<TraineeTrainerRelationshipEntity>> captor =
                ArgumentCaptor.forClass((Class) List.class);
        verify(repository).saveAll(captor.capture());

        List<TraineeTrainerRelationshipEntity> entities = captor.getValue();
        assertThat(entities).hasSize(3)
                .extracting(e -> e.getId().getTrainerId())
                .containsExactlyInAnyOrder("t1", "t2", "t3");

        // ensure method tolerates duplicate and blank entries
    }

    @Test
    void associateTraineeWithTrainers_emptyOrNullList_noop() {
        service.associateTraineeWithTrainers(traineeId, Collections.emptyList());
        service.associateTraineeWithTrainers(traineeId, null);
        verify(repository, never()).saveAll(any());
    }

    @Test
    void associateTraineeWithTrainers_onDataIntegrity_throwsRelationshipCreationException() {
        List<String> trainerIds = List.of("t1", "t2");
        when(repository.saveAll(any())).thenThrow(new DataIntegrityViolationException("dup"));

        assertThatThrownBy(() -> service.associateTraineeWithTrainers(traineeId, trainerIds))
                .isInstanceOf(RelationshipCreationException.class)
                .hasMessageContaining("associateTraineeWithTrainers");

        verify(repository).saveAll(any());
    }

    @Test
    void disassociate_success() {
        when(repository.deleteById_TraineeIdAndId_TrainerId(traineeId, trainerId)).thenReturn(1);

        service.disassociate(traineeId, trainerId);

        verify(repository).deleteById_TraineeIdAndId_TrainerId(traineeId, trainerId);
    }

    @Test
    void disassociate_notFound_throwsRelationshipNotFoundException() {
        when(repository.deleteById_TraineeIdAndId_TrainerId(traineeId, trainerId)).thenReturn(0);

        assertThatThrownBy(() -> service.disassociate(traineeId, trainerId))
                .isInstanceOf(RelationshipNotFoundException.class)
                .hasMessageContaining("Relationship not found");

        verify(repository).deleteById_TraineeIdAndId_TrainerId(traineeId, trainerId);
    }

    @Test
    void exists_delegatesToRepository() {
        when(repository.existsById_TraineeIdAndId_TrainerId(traineeId, trainerId)).thenReturn(true);
        boolean result = service.exists(traineeId, trainerId);
        assertThat(result).isTrue();
        verify(repository).existsById_TraineeIdAndId_TrainerId(traineeId, trainerId);
    }

    @Test
    void getTrainersForTrainee_delegates() {
        when(repository.findTrainerIdsByTraineeId(traineeId)).thenReturn(List.of("t1", "t2"));
        List<String> trainers = service.getTrainersForTrainee(traineeId);
        assertThat(trainers).containsExactly("t1", "t2");
        verify(repository).findTrainerIdsByTraineeId(traineeId);
    }

    @Test
    void getTraineesForTrainer_delegates() {
        when(repository.findTraineeIdsByTrainerId(trainerId)).thenReturn(List.of("tr1", "tr2"));
        List<String> trainees = service.getTraineesForTrainer(trainerId);
        assertThat(trainees).containsExactly("tr1", "tr2");
        verify(repository).findTraineeIdsByTrainerId(trainerId);
    }

    @Test
    void validation_onReadMethods_throwsForBlankId() {
        assertThatThrownBy(() -> service.exists(null, trainerId)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> service.getTrainersForTrainee(" ")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> service.getTraineesForTrainer("")).isInstanceOf(IllegalArgumentException.class);
    }
}
