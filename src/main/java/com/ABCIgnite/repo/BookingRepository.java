package com.ABCIgnite.repo;

import com.ABCIgnite.entities.BookingEntity;
import com.ABCIgnite.entities.ClassEntity;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, String> {
    List<BookingEntity> findByMemberName(String memberName);
    List<BookingEntity> findByParticipationDateBetween(LocalDate startDate, LocalDate endDate);

    List<BookingEntity> findByMemberNameAndParticipationDateBetween(String memberName, LocalDate start, LocalDate end);

    List<BookingEntity> findByClassEntityAndParticipationDate(ClassEntity classEntity, @NotNull(message = "Participation date is required") LocalDate participationDate);
}
