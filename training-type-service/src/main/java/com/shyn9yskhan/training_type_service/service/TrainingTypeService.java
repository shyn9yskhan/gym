package com.shyn9yskhan.training_type_service.service;

import com.shyn9yskhan.training_type_service.dto.CreateTrainingTypeRequest;
import com.shyn9yskhan.training_type_service.dto.CreateTrainingTypeResponse;
import com.shyn9yskhan.training_type_service.dto.GetAllTrainingTypesResponse;
import com.shyn9yskhan.training_type_service.dto.GetTrainingTypeResponse;

public interface TrainingTypeService {
    CreateTrainingTypeResponse createTrainingType(CreateTrainingTypeRequest createTrainingTypeRequest);
    GetTrainingTypeResponse getTrainingType(String trainingTypeId);
    void deleteTrainingType(String trainingTypeId);
    GetAllTrainingTypesResponse getAllTrainingTypes();
}
