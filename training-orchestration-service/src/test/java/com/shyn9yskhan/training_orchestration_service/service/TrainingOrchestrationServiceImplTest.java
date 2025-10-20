package com.shyn9yskhan.training_orchestration_service.service;

import com.shyn9yskhan.training_orchestration_service.client.TraineeServiceClient;
import com.shyn9yskhan.training_orchestration_service.client.TrainerServiceClient;
import com.shyn9yskhan.training_orchestration_service.client.TrainingServiceClient;
import com.shyn9yskhan.training_orchestration_service.client.UserServiceClient;
import com.shyn9yskhan.training_orchestration_service.client.dto.*;
import com.shyn9yskhan.training_orchestration_service.dto.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingOrchestrationServiceImplTest {

    @Mock private TrainingServiceClient trainingServiceClient;
    @Mock private UserServiceClient userServiceClient;
    @Mock private TraineeServiceClient traineeServiceClient;
    @Mock private TrainerServiceClient trainerServiceClient;

    @InjectMocks
    private TrainingOrchestrationServiceImpl service;

    @Test
    void getTraineeTrainingsList_filtersByDateTrainerAndType() {
        String username = "alice";
        String userId = "user-1";
        String traineeId = "trainee-1";

        ResponseEntity<GetUserIdByUsernameServiceResponse> userResp = mock(ResponseEntity.class);
        GetUserIdByUsernameServiceResponse userDto = mock(GetUserIdByUsernameServiceResponse.class);
        when(userDto.userId()).thenReturn(userId);
        when(userResp.getBody()).thenReturn(userDto);
        when(userServiceClient.getUserIdByUsername(username)).thenReturn(userResp);

        ResponseEntity<GetTraineeIdByUserIdServiceResponse> traineeResp = mock(ResponseEntity.class);
        GetTraineeIdByUserIdServiceResponse traineeDto = mock(GetTraineeIdByUserIdServiceResponse.class);
        when(traineeDto.traineeId()).thenReturn(traineeId);
        when(traineeResp.getBody()).thenReturn(traineeDto);
        when(traineeServiceClient.getTraineeIdByUserId(userId)).thenReturn(traineeResp);

        TrainingDto training1 = mock(TrainingDto.class);
        when(training1.date()).thenReturn(LocalDate.of(2025, 6, 10));
        when(training1.trainerId()).thenReturn("trainer-A");
        when(training1.trainingTypeId()).thenReturn("type-1");
        when(training1.traineeId()).thenReturn(traineeId);

        TrainingDto training2 = mock(TrainingDto.class);
        when(training2.date()).thenReturn(LocalDate.of(2025, 7, 1));
        when(training2.trainerId()).thenReturn("trainer-B");
        when(training2.trainingTypeId()).thenReturn("type-2");
        when(training2.traineeId()).thenReturn(traineeId);

        TrainingDto training3 = mock(TrainingDto.class);
        when(training3.date()).thenReturn(LocalDate.of(2025, 6, 20));
        when(training3.trainerId()).thenReturn("trainer-A");
        when(training3.trainingTypeId()).thenReturn("type-1");
        when(training3.traineeId()).thenReturn(traineeId);

        List<TrainingDto> all = List.of(training1, training2, training3);

        ResponseEntity<GetTraineeTrainingsServiceResponse> trainingsResp = mock(ResponseEntity.class);
        GetTraineeTrainingsServiceResponse trainingsBody = mock(GetTraineeTrainingsServiceResponse.class);
        when(trainingsBody.trainings()).thenReturn(all);
        when(trainingsResp.getBody()).thenReturn(trainingsBody);
        when(trainingServiceClient.getTrainingsByTraineeId(traineeId)).thenReturn(trainingsResp);

        GetTraineeTrainingsListRequest request = new GetTraineeTrainingsListRequest(
                username,
                LocalDate.of(2025, 6, 1),
                LocalDate.of(2025, 6, 30),
                "trainer-A",
                "type-1"
        );

        GetTraineeTrainingsListResponse result = service.getTraineeTrainingsList(request);

        assertThat(result).isNotNull();
        assertThat(result.trainings()).hasSize(2);
        assertThat(result.trainings()).containsExactlyInAnyOrder(training1, training3);
    }

    @Test
    void getTraineeTrainingsList_returnsEmptyWhenUpstreamNull() {
        String username = "bob";
        String userId = "user-2";

        ResponseEntity<GetUserIdByUsernameServiceResponse> userResp = mock(ResponseEntity.class);
        GetUserIdByUsernameServiceResponse userDto = mock(GetUserIdByUsernameServiceResponse.class);
        when(userDto.userId()).thenReturn(userId);
        when(userResp.getBody()).thenReturn(userDto);
        when(userServiceClient.getUserIdByUsername(username)).thenReturn(userResp);

        ResponseEntity<GetTraineeIdByUserIdServiceResponse> traineeResp = mock(ResponseEntity.class);
        when(traineeResp.getBody()).thenReturn(null);
        when(traineeServiceClient.getTraineeIdByUserId(userId)).thenReturn(traineeResp);

        GetTraineeTrainingsListRequest request = new GetTraineeTrainingsListRequest(username, null, null, null, null);

        GetTraineeTrainingsListResponse result = service.getTraineeTrainingsList(request);

        assertThat(result).isNotNull();
        assertThat(result.trainings()).isEmpty();
    }

    @Test
    void addTraining_resolvesIdsAndCreatesTraining() {
        String traineeUsername = "traineeUser";
        String trainerUsername = "trainerUser";

        ResponseEntity<GetUserIdByUsernameServiceResponse> traineeUserResp = mock(ResponseEntity.class);
        GetUserIdByUsernameServiceResponse traineeUserDto = mock(GetUserIdByUsernameServiceResponse.class);
        when(traineeUserDto.userId()).thenReturn("u-trainee");
        when(traineeUserResp.getBody()).thenReturn(traineeUserDto);
        when(userServiceClient.getUserIdByUsername(traineeUsername)).thenReturn(traineeUserResp);

        ResponseEntity<GetUserIdByUsernameServiceResponse> trainerUserResp = mock(ResponseEntity.class);
        GetUserIdByUsernameServiceResponse trainerUserDto = mock(GetUserIdByUsernameServiceResponse.class);
        when(trainerUserDto.userId()).thenReturn("u-trainer");
        when(trainerUserResp.getBody()).thenReturn(trainerUserDto);
        when(userServiceClient.getUserIdByUsername(trainerUsername)).thenReturn(trainerUserResp);

        ResponseEntity<GetTraineeIdByUserIdServiceResponse> traineeIdResp = mock(ResponseEntity.class);
        GetTraineeIdByUserIdServiceResponse traineeIdDto = mock(GetTraineeIdByUserIdServiceResponse.class);
        when(traineeIdDto.traineeId()).thenReturn("trainee-42");
        when(traineeIdResp.getBody()).thenReturn(traineeIdDto);
        when(traineeServiceClient.getTraineeIdByUserId("u-trainee")).thenReturn(traineeIdResp);

        ResponseEntity<GetTrainerIdByUserIdServiceResponse> trainerIdResp = mock(ResponseEntity.class);
        GetTrainerIdByUserIdServiceResponse trainerIdDto = mock(GetTrainerIdByUserIdServiceResponse.class);
        when(trainerIdDto.trainerId()).thenReturn("trainer-77");
        when(trainerIdResp.getBody()).thenReturn(trainerIdDto);
        when(trainerServiceClient.getTrainerIdByUserId("u-trainer")).thenReturn(trainerIdResp);

        ResponseEntity<GetTrainingTypeIdByTrainerIdServiceResponse> trainingTypeResp = mock(ResponseEntity.class);
        GetTrainingTypeIdByTrainerIdServiceResponse trainingTypeDto = mock(GetTrainingTypeIdByTrainerIdServiceResponse.class);
        when(trainingTypeDto.trainingTypeId()).thenReturn("type-x");
        when(trainingTypeResp.getBody()).thenReturn(trainingTypeDto);
        when(trainerServiceClient.getTrainingTypeIdByTrainerId("trainer-77")).thenReturn(trainingTypeResp);

        ArgumentCaptor<CreateTrainingServiceRequest> captor = ArgumentCaptor.forClass(CreateTrainingServiceRequest.class);

        AddTrainingRequest addReq = new AddTrainingRequest(
                traineeUsername,
                trainerUsername,
                "Morning session",
                LocalDate.of(2025, 8, 5),
                60
        );

        service.addTraining(addReq);

        verify(trainingServiceClient).createTraining(captor.capture());
        CreateTrainingServiceRequest captured = captor.getValue();
        assertThat(captured.traineeId()).isEqualTo("trainee-42");
        assertThat(captured.trainerId()).isEqualTo("trainer-77");
        assertThat(captured.trainingTypeId()).isEqualTo("type-x");
        assertThat(captured.trainingName()).isEqualTo("Morning session");
        assertThat(captured.date()).isEqualTo(LocalDate.of(2025, 8, 5));
        assertThat(captured.duration()).isEqualTo(60);
    }
}
