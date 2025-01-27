package com.example.foodRating.controller;

import com.example.foodRating.entity.MealType;
import com.example.foodRating.service.FoodRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;

@RestController
@RequestMapping("/api/food-ratings")
public class FoodRatingController {

    @Autowired
    private FoodRatingService foodRatingService;

    @PostMapping("/rate")
    public String rateMeal(
            @RequestParam Long userId,
            @RequestParam String mealType,
            @RequestParam int rating,
            @RequestParam String feedback) {

        MealType mealTypeEnum = MealType.valueOf(mealType.toUpperCase());
        foodRatingService.saveRating(userId, mealTypeEnum, rating, feedback);
        return "Rating submitted successfully for " + mealType;
    }

    @GetMapping("/average")
    public ResponseEntity<Double> getAverageRating(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(foodRatingService.getAverageRating(date));
    }

    @GetMapping("/stats")
    public String getDailyStatistics(@RequestParam String date) {
        LocalDate localDate = LocalDate.parse(date);
        return foodRatingService.getDailyStatistics(localDate);
    }

    @GetMapping("/summarize-feedback")
    public ResponseEntity<String> summarizeFeedback(
            @RequestParam String date,
            @RequestParam(required = false) String mealType) {
        LocalDate localDate = LocalDate.parse(date);
        MealType mealTypeEnum = (mealType != null) ? MealType.valueOf(mealType.toUpperCase()) : null;
        String summarizedFeedback = foodRatingService.summarizeFeedback(localDate, mealTypeEnum);
        return ResponseEntity.ok(summarizedFeedback);
    }

    @GetMapping("/today")
    public ResponseEntity<String> getTodayFeedback() {
        LocalDate today = LocalDate.now();
        MealType currentMealType = determineCurrentMealType();
        String summarizedFeedback = foodRatingService.summarizeFeedback(today, currentMealType);
        return ResponseEntity.ok("Feedback for " + currentMealType + ": " + summarizedFeedback);
    }

    private MealType determineCurrentMealType() {
        int currentHour = LocalTime.now().getHour();
        if (currentHour >= 6 && currentHour < 11) {
            return MealType.BREAKFAST;
        } else if (currentHour >= 11 && currentHour < 16) {
            return MealType.LUNCH;
        } else if (currentHour >= 16 && currentHour < 19) {
            return MealType.SNACKS;
        } else {
            return MealType.DINNER;
        }
    }
}