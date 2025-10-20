package com.shyn9yskhan.trainer_workload_service.service;

import com.shyn9yskhan.trainer_workload_service.domain.MonthSummary;
import com.shyn9yskhan.trainer_workload_service.domain.YearSummary;
import com.shyn9yskhan.trainer_workload_service.dto.TrainerMonthlySummaryResponse;
import com.shyn9yskhan.trainer_workload_service.dto.WorkloadEventRequest;
import com.shyn9yskhan.trainer_workload_service.domain.WorkloadAction;
import com.shyn9yskhan.trainer_workload_service.entity.TrainerEntity;
import com.shyn9yskhan.trainer_workload_service.entity.TrainerMonthSummaryEntity;
import com.shyn9yskhan.trainer_workload_service.repository.TrainerMonthSummaryRepository;
import com.shyn9yskhan.trainer_workload_service.repository.TrainerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerWorkloadServiceImplTest {

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private TrainerMonthSummaryRepository trainerMonthSummaryRepository;

    @InjectMocks
    private TrainerWorkloadServiceImpl service;

    private final String username = "trainerA";
    private final String firstname = "John";
    private final String lastname = "Doe";

    @BeforeEach
    void setUp() {
    }

    @Test
    void acceptWorkloadEvent_add_createsNewMonthRowWhenMissing() {
        LocalDate date = LocalDate.of(2025, 6, 15);
        WorkloadEventRequest req = new WorkloadEventRequest(username, firstname, lastname, true, date, 90, WorkloadAction.ADD);

        when(trainerRepository.findByTrainerUsername(username)).thenReturn(Optional.empty());
        when(trainerMonthSummaryRepository.findByTrainerUsernameAndYearAndMonth(username, 2025, 6)).thenReturn(Optional.empty());

        service.acceptWorkloadEvent(req);

        ArgumentCaptor<TrainerEntity> trainerCaptor = ArgumentCaptor.forClass(TrainerEntity.class);
        verify(trainerRepository).save(trainerCaptor.capture());
        assertThat(trainerCaptor.getValue().getTrainerUsername()).isEqualTo(username);
        assertThat(trainerCaptor.getValue().getTrainerFirstname()).isEqualTo(firstname);
        assertThat(trainerCaptor.getValue().getTrainerLastname()).isEqualTo(lastname);
        assertThat(trainerCaptor.getValue().isActive()).isTrue();

        ArgumentCaptor<TrainerMonthSummaryEntity> monthCaptor = ArgumentCaptor.forClass(TrainerMonthSummaryEntity.class);
        verify(trainerMonthSummaryRepository).save(monthCaptor.capture());
        TrainerMonthSummaryEntity saved = monthCaptor.getValue();
        assertThat(saved.getTrainerUsername()).isEqualTo(username);
        assertThat(saved.getYear()).isEqualTo(2025);
        assertThat(saved.getMonth()).isEqualTo(6);
        assertThat(saved.getTotalDurationMinutes()).isEqualTo(90L);
    }

    @Test
    void acceptWorkloadEvent_add_incrementsExistingMonthRow() {
        LocalDate date = LocalDate.of(2025, 6, 5);
        WorkloadEventRequest req = new WorkloadEventRequest(username, firstname, lastname, true, date, 30, WorkloadAction.ADD);

        TrainerMonthSummaryEntity existing = new TrainerMonthSummaryEntity(username, 2025, 6, 120);
        when(trainerRepository.findByTrainerUsername(username)).thenReturn(Optional.of(new TrainerEntity(username, firstname, lastname, true)));
        when(trainerMonthSummaryRepository.findByTrainerUsernameAndYearAndMonth(username, 2025, 6)).thenReturn(Optional.of(existing));

        service.acceptWorkloadEvent(req);

        assertThat(existing.getTotalDurationMinutes()).isEqualTo(150L);
        verify(trainerMonthSummaryRepository).save(existing);
        verify(trainerRepository, never()).save(any());
    }

    @Test
    void acceptWorkloadEvent_delete_reducesAndDeletesWhenZeroOrLess() {
        LocalDate date = LocalDate.of(2025, 7, 10);
        WorkloadEventRequest req = new WorkloadEventRequest(username, firstname, lastname, true, date, 60, WorkloadAction.DELETE);

        TrainerMonthSummaryEntity existing = new TrainerMonthSummaryEntity(username, 2025, 7, 60);
        when(trainerRepository.findByTrainerUsername(username)).thenReturn(Optional.of(new TrainerEntity(username, firstname, lastname, true)));
        when(trainerMonthSummaryRepository.findByTrainerUsernameAndYearAndMonth(username, 2025, 7)).thenReturn(Optional.of(existing));

        service.acceptWorkloadEvent(req);

        verify(trainerMonthSummaryRepository).delete(existing);
        verify(trainerMonthSummaryRepository, never()).save(argThat(e -> e.getYear() == 2025 && e.getMonth() == 7));
    }

    @Test
    void acceptWorkloadEvent_delete_reducesAndSavesWhenPositive() {
        LocalDate date = LocalDate.of(2025, 7, 10);
        WorkloadEventRequest req = new WorkloadEventRequest(username, firstname, lastname, true, date, 30, WorkloadAction.DELETE);

        TrainerMonthSummaryEntity existing = new TrainerMonthSummaryEntity(username, 2025, 7, 90);
        when(trainerRepository.findByTrainerUsername(username)).thenReturn(Optional.of(new TrainerEntity(username, firstname, lastname, true)));
        when(trainerMonthSummaryRepository.findByTrainerUsernameAndYearAndMonth(username, 2025, 7)).thenReturn(Optional.of(existing));

        service.acceptWorkloadEvent(req);

        assertThat(existing.getTotalDurationMinutes()).isEqualTo(60L);
        verify(trainerMonthSummaryRepository).save(existing);
        verify(trainerMonthSummaryRepository, never()).delete(existing);
    }

    @Test
    void acceptWorkloadEvent_upsert_updatesMetadataWhenChanged() {
        LocalDate date = LocalDate.of(2025, 8, 1);
        WorkloadEventRequest req = new WorkloadEventRequest(username, "NewFirst", "NewLast", false, date, 15, WorkloadAction.ADD);

        TrainerEntity existingTrainer = new TrainerEntity(username, firstname, lastname, true);
        when(trainerRepository.findByTrainerUsername(username)).thenReturn(Optional.of(existingTrainer));
        when(trainerMonthSummaryRepository.findByTrainerUsernameAndYearAndMonth(username, 2025, 8)).thenReturn(Optional.empty());

        service.acceptWorkloadEvent(req);

        assertThat(existingTrainer.getTrainerFirstname()).isEqualTo("NewFirst");
        assertThat(existingTrainer.getTrainerLastname()).isEqualTo("NewLast");
        assertThat(existingTrainer.isActive()).isFalse();
        verify(trainerRepository).save(existingTrainer);

        verify(trainerMonthSummaryRepository).save(any(TrainerMonthSummaryEntity.class));
    }

    @Test
    void acceptWorkloadEvent_invalidInput_isIgnored() {
        service.acceptWorkloadEvent(null);
        WorkloadEventRequest reqNoUser = new WorkloadEventRequest(null, firstname, lastname, true, LocalDate.now(), 10, WorkloadAction.ADD);
        service.acceptWorkloadEvent(reqNoUser);
        WorkloadEventRequest reqNoDate = new WorkloadEventRequest(username, firstname, lastname, true, null, 10, WorkloadAction.ADD);
        service.acceptWorkloadEvent(reqNoDate);

        verifyNoInteractions(trainerRepository);
        verifyNoInteractions(trainerMonthSummaryRepository);
    }

    @Test
    void getTrainerSummary_nullOrBlankUsername_returnsEmptyResponse() {
        TrainerMonthlySummaryResponse resp1 = service.getTrainerSummary(null);
        assertThat(resp1).isNotNull();
        assertThat(resp1.trainerUsername()).isEmpty();
        assertThat(resp1.years()).isEmpty();

        TrainerMonthlySummaryResponse resp2 = service.getTrainerSummary(" ");
        assertThat(resp2).isNotNull();
        assertThat(resp2.trainerUsername()).isEmpty();
        assertThat(resp2.years()).isEmpty();
    }

    @Test
    void getTrainerSummary_trainerNotFound_returnsEmptyYears() {
        when(trainerRepository.findByTrainerUsername(username)).thenReturn(Optional.empty());
        TrainerMonthlySummaryResponse resp = service.getTrainerSummary(username);

        assertThat(resp).isNotNull();
        assertThat(resp.trainerUsername()).isEqualTo(username);
        assertThat(resp.years()).isEmpty();
    }

    @Test
    void getTrainerSummary_returnsGroupedYearsAndMonths() {
        TrainerEntity trainer = new TrainerEntity(username, firstname, lastname, true);
        when(trainerRepository.findByTrainerUsername(username)).thenReturn(Optional.of(trainer));

        TrainerMonthSummaryEntity row1 = new TrainerMonthSummaryEntity(username, 2024, 12, 60);
        TrainerMonthSummaryEntity row2 = new TrainerMonthSummaryEntity(username, 2025, 1, 30);
        TrainerMonthSummaryEntity row3 = new TrainerMonthSummaryEntity(username, 2025, 2, 45);
        when(trainerMonthSummaryRepository.findByTrainerUsername(username)).thenReturn(List.of(row1, row2, row3));

        TrainerMonthlySummaryResponse resp = service.getTrainerSummary(username);

        assertThat(resp).isNotNull();
        assertThat(resp.trainerUsername()).isEqualTo(username);
        assertThat(resp.trainerFirstname()).isEqualTo(firstname);
        assertThat(resp.trainerLastname()).isEqualTo(lastname);
        assertThat(resp.isActive()).isTrue();

        List<YearSummary> years = resp.years();
        assertThat(years).hasSize(2);
        assertThat(years.get(0).year()).isEqualTo(2024);
        assertThat(years.get(0).months()).containsExactly(new MonthSummary(12, 60));
        assertThat(years.get(1).year()).isEqualTo(2025);
        assertThat(years.get(1).months()).containsExactly(new MonthSummary(1, 30), new MonthSummary(2, 45));
    }
}
