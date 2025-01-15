package com.ABCIgnite.repo;


import com.ABCIgnite.entities.ClassEntity;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class ClassRepository {

//    Using interface instead of implementation
    private final ConcurrentMap<String, ClassEntity> classStorage = new ConcurrentHashMap<>();

    public void save(String className, ClassEntity classEntity) {
        classStorage.put(className, classEntity);
    }

    public ClassEntity findByName(String className) {
        return classStorage.get(className);
    }

    public ConcurrentMap<String, ClassEntity> getAllClasses() {
        return classStorage;
    }
}
