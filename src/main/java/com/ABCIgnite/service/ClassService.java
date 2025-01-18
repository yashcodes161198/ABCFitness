package com.ABCIgnite.service;

import com.ABCIgnite.dto.request.ClassRequestDTO;
import com.ABCIgnite.dto.response.ClassResponseDTO;
import com.ABCIgnite.entities.ClassEntity;
import com.ABCIgnite.repo.ClassRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClassService {
    private final ClassRepository classRepository;

    public ClassService(ClassRepository classRepository) {
        this.classRepository = classRepository;
    }

    public synchronized ClassResponseDTO createClass(ClassRequestDTO classRequestDTO) {
        if (classRequestDTO.getEndDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("End date must be in the future");
        }
        if (classRequestDTO.getCapacity() < 1) {
            throw new IllegalArgumentException("Capacity must be at least 1");
        }

        ClassEntity classEntity = new ClassEntity(
                classRequestDTO.getName(),
                classRequestDTO.getStartDate(),
                classRequestDTO.getEndDate(),
                classRequestDTO.getStartTime(),
                classRequestDTO.getDuration(),
                classRequestDTO.getCapacity()
        );

        classRepository.save(classEntity);

        return new ClassResponseDTO(
                classEntity.getClassId(),
                classEntity.getName(),
                classEntity.getStartDate(),
                classEntity.getEndDate(),
                classEntity.getStartTime(),
                classEntity.getDuration(),
                classEntity.getCapacity()
        );
    }

    public List<ClassResponseDTO> getAllClasses() {
        return classRepository.findAll().stream()
                .map(classEntity -> new ClassResponseDTO(
                        classEntity.getClassId(),
                        classEntity.getName(),
                        classEntity.getStartDate(),
                        classEntity.getEndDate(),
                        classEntity.getStartTime(),
                        classEntity.getDuration(),
                        classEntity.getCapacity()
                ))
                .collect(Collectors.toList());
    }
}
