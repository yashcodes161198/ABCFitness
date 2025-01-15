package com.ABCIgnite.service;

import com.ABCIgnite.entities.ClassEntity;
import com.ABCIgnite.repo.ClassRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ClassService {

    private final ClassRepository classRepository;

    public ClassService(ClassRepository classRepository) {
        this.classRepository = classRepository;
    }

    public void createClass(ClassEntity classEntity) {
        if (classEntity.getEndDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("End date must be in the future");
        }
        classRepository.save(classEntity.getName(), classEntity);
    }
}
