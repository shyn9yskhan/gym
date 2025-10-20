package com.shyn9yskhan.training_type_service.service;

import com.shyn9yskhan.training_type_service.dto.*;
import com.shyn9yskhan.training_type_service.entity.TrainingTypeEntity;
import com.shyn9yskhan.training_type_service.repository.TrainingTypeRepository;
import com.shyn9yskhan.training_type_service.service.exception.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingTypeServiceImplTest {

    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @InjectMocks
    private TrainingTypeServiceImpl trainingTypeService;

    @Test
    void createTrainingType_success() {
        CreateTrainingTypeRequest req = new CreateTrainingTypeRequest("Cardio");
        TrainingTypeEntity saved = new TrainingTypeEntity("id-1", "Cardio");
        when(trainingTypeRepository.save(any(TrainingTypeEntity.class))).thenReturn(saved);

        CreateTrainingTypeResponse resp = trainingTypeService.createTrainingType(req);

        assertThat(resp).isNotNull();
        assertThat(resp.id()).isEqualTo("id-1");
        assertThat(resp.trainingTypeName()).isEqualTo("Cardio");
        verify(trainingTypeRepository).save(any(TrainingTypeEntity.class));
    }

    @Test
    void createTrainingType_validationFails_whenNullRequest() {
        assertThatThrownBy(() -> trainingTypeService.createTrainingType(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("CreateTrainingTypeRequest cannot be null");
    }

    @Test
    void createTrainingType_validationFails_whenNameBlank() {
        CreateTrainingTypeRequest req = new CreateTrainingTypeRequest(" ");
        assertThatThrownBy(() -> trainingTypeService.createTrainingType(req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Training type name is required");
    }

    @Test
    void getTrainingType_success() {
        String id = "tt-1";
        TrainingTypeEntity entity = new TrainingTypeEntity(id, "Strength");
        when(trainingTypeRepository.findById(id)).thenReturn(Optional.of(entity));

        GetTrainingTypeResponse resp = trainingTypeService.getTrainingType(id);

        assertThat(resp).isNotNull();
        assertThat(resp.trainingTypeName()).isEqualTo("Strength");
        verify(trainingTypeRepository).findById(id);
    }

    @Test
    void getTrainingType_notFound_throws() {
        when(trainingTypeRepository.findById("missing")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> trainingTypeService.getTrainingType("missing"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Training type not found with ID");
    }

    @Test
    void deleteTrainingType_success() {
        when(trainingTypeRepository.existsById("t1")).thenReturn(true);
        doNothing().when(trainingTypeRepository).deleteById("t1");

        trainingTypeService.deleteTrainingType("t1");

        verify(trainingTypeRepository).existsById("t1");
        verify(trainingTypeRepository).deleteById("t1");
    }

    @Test
    void deleteTrainingType_notFound_throws() {
        when(trainingTypeRepository.existsById("x")).thenReturn(false);
        assertThatThrownBy(() -> trainingTypeService.deleteTrainingType("x"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Training type not found with ID");
    }

    @Test
    void getAllTrainingTypes_returnsList() {
        TrainingTypeEntity e1 = new TrainingTypeEntity("id1", "A");
        TrainingTypeEntity e2 = new TrainingTypeEntity("id2", "B");
        when(trainingTypeRepository.findAll()).thenReturn(List.of(e1, e2));

        GetAllTrainingTypesResponse resp = trainingTypeService.getAllTrainingTypes();

        assertThat(resp).isNotNull();
        assertThat(resp.trainingTypes()).hasSize(2);
        assertThat(resp.trainingTypes().get(0).trainingTypeName()).isEqualTo("A");
    }
}
