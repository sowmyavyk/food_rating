package com.example.foodRating.repository;

import com.example.foodRating.entity.StudentRating;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface StudentRatingRepository extends JpaRepository<StudentRating, Long> {
    List<StudentRating> findByDateAndMealType(LocalDate date, String mealType);
    List<StudentRating> findByRollNumberAndDate(String rollNumber, LocalDate date);
}
