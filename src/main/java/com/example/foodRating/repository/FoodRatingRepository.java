package com.example.foodRating.repository;

import com.example.foodRating.entity.FoodRating;
import com.example.foodRating.entity.MealType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FoodRatingRepository extends JpaRepository<FoodRating, Long> {
    List<FoodRating> findAllByDate(LocalDate date);

    List<FoodRating> findAllByDateAndMealType(LocalDate date, MealType mealType);

    @Query("SELECT AVG(r.rating) FROM FoodRating r WHERE r.date = :date")
    Double findAverageRatingByDate(@Param("date") LocalDate date);

    @Query("SELECT COUNT(DISTINCT r.userId) FROM FoodRating r WHERE r.date = :date")
    Long countDistinctUsersByDate(@Param("date") LocalDate date);

    @Query("SELECT COUNT(r) FROM FoodRating r WHERE r.date = :date")
    Long countTotalRatingsByDate(@Param("date") LocalDate date);
}