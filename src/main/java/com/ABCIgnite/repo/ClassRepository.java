package com.ABCIgnite.repo;

import com.ABCIgnite.entities.ClassEntity;
import jakarta.persistence.LockModeType;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClassRepository extends JpaRepository<ClassEntity, String> {
    ClassEntity findByName(String name);


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM ClassEntity c WHERE c.classId = :classId")
    Optional<ClassEntity> findByClassIdWithLock(String classId);

//    ClassEntity findByClassId(@NotNull(message = "Class Id is required") String classId);
}
