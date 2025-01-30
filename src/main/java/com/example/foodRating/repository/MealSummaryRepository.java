package com.example.foodRating.repository;

import com.example.foodRating.entity.MealSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MealSummaryRepository extends JpaRepository<MealSummary, Long> {
    Optional<MealSummary> findByDateAndMealType(LocalDate date, String mealType); // Returns Optional<MealSummary>
    List<MealSummary> findByDate(LocalDate date);
}
