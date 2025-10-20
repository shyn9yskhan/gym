package com.shyn9yskhan.trainee_service.service;

import com.shyn9yskhan.trainee_service.dto.*;
import com.shyn9yskhan.trainee_service.entity.TraineeEntity;
import com.shyn9yskhan.trainee_service.repository.TraineeRepository;
import com.shyn9yskhan.trainee_service.service.exception.EntityNotFoundException;
import com.shyn9yskhan.trainee_service.service.exception.TraineeCreationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraineeServiceImplTest {

    @Mock
    private TraineeRepository traineeRepository;

    @InjectMocks
    private TraineeServiceImpl traineeService;

    private LocalDate sampleDob;

    @BeforeEach
    void setUp() {
        sampleDob = LocalDate.of(1990, 1, 1);
    }

    @Test
    void createTrainee_success() {
        CreateTraineeRequest request = new CreateTraineeRequest(sampleDob, "addr", "user-123");

        TraineeEntity saved = new TraineeEntity(UUID.randomUUID().toString(), sampleDob, "addr", "user-123");
        when(traineeRepository.save(any(TraineeEntity.class))).thenReturn(saved);

        CreateTraineeResponse response = traineeService.createTrainee(request);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(saved.getId());
        assertThat(response.dateOfBirth()).isEqualTo(sampleDob);
        assertThat(response.address()).isEqualTo("addr");

        ArgumentCaptor<TraineeEntity> captor = ArgumentCaptor.forClass(TraineeEntity.class);
        verify(traineeRepository).save(captor.capture());
        TraineeEntity toSave = captor.getValue();
        assertThat(toSave.getUserId()).isEqualTo("user-123");
        assertThat(toSave.getAddress()).isEqualTo("addr");
    }

    @Test
    void createTrainee_whenSaveThrowsDataIntegrity_thenTraineeCreationException() {
        CreateTraineeRequest request = new CreateTraineeRequest(sampleDob, "addr", "user-123");
        when(traineeRepository.save(any())).thenThrow(new DataIntegrityViolationException("dup"));

        assertThatThrownBy(() -> traineeService.createTrainee(request))
                .isInstanceOf(TraineeCreationException.class)
                .hasMessageContaining("Failed to create trainee");

        verify(traineeRepository).save(any());
    }

    @Test
    void getTrainee_success() {
        String traineeId = "t-1";
        TraineeEntity entity = new TraineeEntity(traineeId, sampleDob, "addr", "user-123");
        when(traineeRepository.findById(traineeId)).thenReturn(Optional.of(entity));

        GetTraineeResponse response = traineeService.getTrainee(traineeId);

        assertThat(response).isNotNull();
        assertThat(response.dateOfBirth()).isEqualTo(sampleDob);
        assertThat(response.address()).isEqualTo("addr");
    }

    @Test
    void getTrainee_notFound_throws() {
        when(traineeRepository.findById("missing")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> traineeService.getTrainee("missing"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Trainee not found");
    }

    @Test
    void getTraineeIdByUserId_success() {
        String userId = "user-42";
        String expectedTraineeId = "trainee-42";
        when(traineeRepository.findIdByUserId(userId)).thenReturn(Optional.of(expectedTraineeId));

        GetTraineeIdByUserIdResponse resp = traineeService.getTraineeIdByUserId(userId);

        assertThat(resp).isNotNull();
        assertThat(resp.traineeId()).isEqualTo(expectedTraineeId);
    }

    @Test
    void getTraineeIdByUserId_notFound_throws() {
        when(traineeRepository.findIdByUserId("nope")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> traineeService.getTraineeIdByUserId("nope"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("TraineeID not found");
    }

    @Test
    void getTraineesByIds_mapsEntitiesToDtos() {
        TraineeEntity e1 = new TraineeEntity("id-1", sampleDob, "a1", "user1");
        TraineeEntity e2 = new TraineeEntity("id-2", sampleDob.plusDays(1), "a2", "user2");
        when(traineeRepository.findByIdIn(List.of("id-1", "id-2"))).thenReturn(List.of(e1, e2));

        List<TraineeDto> dtos = traineeService.getTraineesByIds(List.of("id-1", "id-2"));

        assertThat(dtos).hasSize(2);
        assertThat(dtos).extracting("id").containsExactlyInAnyOrder("id-1", "id-2");
    }

    @Test
    void updateTrainee_success() {
        String traineeId = "t-upd";
        UpdateTraineeRequest updateReq = new UpdateTraineeRequest(sampleDob.plusYears(1), "new-addr");

        when(traineeRepository.updateTrainee(eq(traineeId), any(), any())).thenReturn(1);
        TraineeEntity updated = new TraineeEntity(traineeId, updateReq.dateOfBirth(), updateReq.address(), "user-1");
        when(traineeRepository.findById(traineeId)).thenReturn(Optional.of(updated));

        UpdateTraineeResponse resp = traineeService.updateTrainee(traineeId, updateReq);

        assertThat(resp.dateOfBirth()).isEqualTo(updateReq.dateOfBirth());
        assertThat(resp.address()).isEqualTo(updateReq.address());
    }

    @Test
    void updateTrainee_notFound_throws() {
        String traineeId = "t-not";
        UpdateTraineeRequest updateReq = new UpdateTraineeRequest(sampleDob.plusYears(1), "new-addr");
        when(traineeRepository.updateTrainee(eq(traineeId), any(), any())).thenReturn(0);

        assertThatThrownBy(() -> traineeService.updateTrainee(traineeId, updateReq))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Trainee not found");
    }

    @Test
    void deleteTrainee_notFound_throws() {
        when(traineeRepository.deleteTraineeById("missing")).thenReturn(0);
        assertThatThrownBy(() -> traineeService.deleteTrainee("missing"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Trainee not found");
    }

    @Test
    void getTraineeByUserId_success() {
        String userId = "user-99";
        TraineeEntity entity = new TraineeEntity("tid-99", sampleDob, "addr99", userId);
        when(traineeRepository.findByUserId(userId)).thenReturn(Optional.of(entity));

        GetTraineeByUserIdResponse resp = traineeService.getTraineeByUserId(userId);

        assertThat(resp).isNotNull();
        assertThat(resp.id()).isEqualTo("tid-99");
        assertThat(resp.userId()).isEqualTo(userId);
    }

    @Test
    void updateTraineeByUserId_success() {
        String userId = "user-u";
        UpdateTraineeRequest updateReq = new UpdateTraineeRequest(sampleDob, "addrX");
        when(traineeRepository.updateTraineeByUserId(eq(userId), any(), any())).thenReturn(1);
        TraineeEntity entity = new TraineeEntity("tid-u", updateReq.dateOfBirth(), updateReq.address(), userId);
        when(traineeRepository.findByUserId(userId)).thenReturn(Optional.of(entity));

        UpdateTraineeByUserIdResponse resp = traineeService.updateTraineeByUserId(userId, updateReq);

        assertThat(resp.traineeId()).isEqualTo("tid-u");
        assertThat(resp.dateOfBirth()).isEqualTo(updateReq.dateOfBirth());
    }

    @Test
    void deleteTraineeByUserId_notFound_throws() {
        when(traineeRepository.deleteTraineeByUserIdQuery("u-miss")).thenReturn(0);
        assertThatThrownBy(() -> traineeService.deleteTraineeByUserId("u-miss"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Trainee not found");
    }
}
