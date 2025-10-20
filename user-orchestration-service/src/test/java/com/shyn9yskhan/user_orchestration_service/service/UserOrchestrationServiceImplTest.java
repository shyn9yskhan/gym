package com.shyn9yskhan.user_orchestration_service.service;

import com.shyn9yskhan.user_orchestration_service.client.TraineeServiceClient;
import com.shyn9yskhan.user_orchestration_service.client.TraineeTrainerRelationshipServiceClient;
import com.shyn9yskhan.user_orchestration_service.client.TrainerServiceClient;
import com.shyn9yskhan.user_orchestration_service.client.UserServiceClient;
import com.shyn9yskhan.user_orchestration_service.client.dto.trainee.GetTraineeByUserIdServiceResponse;
import com.shyn9yskhan.user_orchestration_service.client.dto.trainer.GetTrainerByUserIdServiceResponse;
import com.shyn9yskhan.user_orchestration_service.client.dto.trainer.TrainerServiceDto;
import com.shyn9yskhan.user_orchestration_service.client.dto.user.UserDto;
import com.shyn9yskhan.user_orchestration_service.client.dto.user.GetUserServiceResponse;
import com.shyn9yskhan.user_orchestration_service.dto.trainee_trainer_relationship.GetNotAssignedOnTraineeActiveTrainersResponse;
import com.shyn9yskhan.user_orchestration_service.dto.trainee_trainer_relationship.UpdateTraineesTrainerListRequest;
import com.shyn9yskhan.user_orchestration_service.dto.trainee_trainer_relationship.UpdateTraineesTrainerListResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserOrchestrationServiceImplTest {

    @Mock
    UserServiceClient userServiceClient;

    @Mock
    TraineeServiceClient traineeServiceClient;

    @Mock
    TrainerServiceClient trainerServiceClient;

    @Mock
    TraineeTrainerRelationshipServiceClient traineeTrainerRelationshipServiceClient;

    @InjectMocks
    UserOrchestrationServiceImpl service;

    @BeforeEach
    void init() {
    }

    @Test
    void updateTraineesTrainerList_happyPath_returnsUpdatedTrainerDtos() {
        String username = "traineeUser";

        GetUserServiceResponse userResp = mock(GetUserServiceResponse.class);
        when(userResp.getId()).thenReturn("user-1");
        when(userServiceClient.getUserByUsername(username)).thenReturn(ResponseEntity.ok(userResp));

        GetTraineeByUserIdServiceResponse traineeResp = mock(GetTraineeByUserIdServiceResponse.class);
        when(traineeResp.getId()).thenReturn("trainee-1");
        when(traineeServiceClient.getTraineeByUserId("user-1")).thenReturn(ResponseEntity.ok(traineeResp));

        List<String> trainerUsernames = List.of("trA", "trB");
        UpdateTraineesTrainerListRequest request = new UpdateTraineesTrainerListRequest(trainerUsernames);

        UserDto uA = mock(UserDto.class);
        when(uA.getUserId()).thenReturn("uA");
        when(uA.getUsername()).thenReturn("trA");
        when(uA.getFirstname()).thenReturn("FirstA");
        when(uA.getLastname()).thenReturn("LastA");

        UserDto uB = mock(UserDto.class);
        when(uB.getUserId()).thenReturn("uB");
        when(uB.getUsername()).thenReturn("trB");
        when(uB.getFirstname()).thenReturn("FirstB");
        when(uB.getLastname()).thenReturn("LastB");

        when(userServiceClient.getUsersByUsernames(trainerUsernames)).thenReturn(ResponseEntity.ok(List.of(uA, uB)));

        TrainerServiceDto tsA = mock(TrainerServiceDto.class);
        when(tsA.getId()).thenReturn("trainer-A");
        when(tsA.getUserId()).thenReturn("uA");
        when(tsA.getSpecialization()).thenReturn("specA");

        TrainerServiceDto tsB = mock(TrainerServiceDto.class);
        when(tsB.getId()).thenReturn("trainer-B");
        when(tsB.getUserId()).thenReturn("uB");
        when(tsB.getSpecialization()).thenReturn("specB");

        when(trainerServiceClient.getTrainersByUserIds(List.of("uA", "uB"))).thenReturn(ResponseEntity.ok(List.of(tsA, tsB)));


        when(traineeTrainerRelationshipServiceClient.getTrainersForTrainee("trainee-1")).thenReturn(ResponseEntity.ok(List.of("trainer-A", "trainer-B")));
        when(trainerServiceClient.getTrainersByIds(List.of("trainer-A", "trainer-B"))).thenReturn(ResponseEntity.ok(List.of(tsA, tsB)));
        when(userServiceClient.getUsersByIds(List.of("uA", "uB"))).thenReturn(ResponseEntity.ok(List.of(uA, uB)));

        UpdateTraineesTrainerListResponse resp = service.updateTraineesTrainerList(username, request);

        assertThat(resp).isNotNull();
        assertThat(resp.trainers()).hasSize(2);
        assertThat(resp.trainers().get(0).username()).isEqualTo("trA");

        verify(traineeTrainerRelationshipServiceClient).associateTraineeWithTrainers("trainee-1", List.of("trainer-A", "trainer-B"));
    }

    @Test
    void getNotAssignedOnTraineeActiveTrainers_filtersAssigned() {
        String traineeUsername = "bob";

        UserDto active1 = mock(UserDto.class);
        when(active1.getUserId()).thenReturn("u1");
        when(active1.getUsername()).thenReturn("assigned");
        when(active1.getFirstname()).thenReturn("A");
        when(active1.getLastname()).thenReturn("One");

        UserDto active2 = mock(UserDto.class);
        when(active2.getUserId()).thenReturn("u2");
        when(active2.getUsername()).thenReturn("free");
        when(active2.getFirstname()).thenReturn("B");
        when(active2.getLastname()).thenReturn("Two");

        when(userServiceClient.getAllActiveUsers()).thenReturn(ResponseEntity.ok(List.of(active1, active2)));

        TrainerServiceDto tsU1 = mock(TrainerServiceDto.class);
        when(tsU1.getUserId()).thenReturn("u1");
        when(tsU1.getSpecialization()).thenReturn("s1");
        when(tsU1.getId()).thenReturn("trainer-1");

        TrainerServiceDto tsU2 = mock(TrainerServiceDto.class);
        when(tsU2.getUserId()).thenReturn("u2");
        when(tsU2.getSpecialization()).thenReturn("s2");
        when(tsU2.getId()).thenReturn("trainer-2");

        when(userServiceClient.getUserByUsername(traineeUsername)).thenReturn(ResponseEntity.ok(mock(GetUserServiceResponse.class)));
        GetUserServiceResponse getUserResp = userServiceClient.getUserByUsername(traineeUsername).getBody();
        when(getUserResp.getId()).thenReturn("user-b");

        GetTraineeByUserIdServiceResponse traineeResp = mock(GetTraineeByUserIdServiceResponse.class);
        when(traineeResp.getId()).thenReturn("trainee-b");
        when(traineeServiceClient.getTraineeByUserId("user-b")).thenReturn(ResponseEntity.ok(traineeResp));

        when(traineeTrainerRelationshipServiceClient.getTrainersForTrainee("trainee-b")).thenReturn(ResponseEntity.ok(List.of("trainer-1")));

        when(trainerServiceClient.getTrainersByIds(eq(List.of("u1", "u2")))).thenReturn(ResponseEntity.ok(List.of(tsU1, tsU2)));
        when(trainerServiceClient.getTrainersByIds(eq(List.of("trainer-1")))).thenReturn(ResponseEntity.ok(List.of(tsU1)));

        when(userServiceClient.getUsersByIds(eq(List.of("u1")))).thenReturn(ResponseEntity.ok(List.of(active1)));

        GetNotAssignedOnTraineeActiveTrainersResponse resp = service.getNotAssignedOnTraineeActiveTrainers(traineeUsername);

        assertThat(resp).isNotNull();
    }

}
