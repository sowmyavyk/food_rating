package com.example.foodRating.service;

import com.example.foodRating.entity.FoodRating;
import com.example.foodRating.entity.MealType;
import com.example.foodRating.repository.FoodRatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FoodRatingService {

    @Autowired
    private FoodRatingRepository repository;

    public void saveRating(Long userId, MealType mealType, int rating, String feedback) {
        if (feedback != null && feedback.length() > 180) {
            throw new IllegalArgumentException("Feedback cannot exceed 180 characters.");
        }

        FoodRating foodRating = new FoodRating();
        foodRating.setUserId(userId);
        foodRating.setDate(LocalDate.now());
        foodRating.setMealType(mealType);
        foodRating.setRating(rating);
        foodRating.setFeedback(feedback);

        repository.save(foodRating);
    }

    public double getAverageRating(LocalDate date) {
        Double averageRating = repository.findAverageRatingByDate(date);
        return (averageRating != null) ? averageRating : 0.0;
    }

    public String getDailyStatistics(LocalDate date) {
        Long totalUsers = repository.countDistinctUsersByDate(date);
        Long totalRatings = repository.countTotalRatingsByDate(date);
        return totalRatings + " marked their rating today, " + totalUsers + " had their food today.";
    }

    public String summarizeFeedback(LocalDate date, MealType mealType) {
        List<FoodRating> ratings;
        if (mealType != null) {
            ratings = repository.findAllByDateAndMealType(date, mealType);
        } else {
            ratings = repository.findAllByDate(date);
        }

        Map<String, Integer> wordFrequency = new HashMap<>();
        StringBuilder combinedFeedback = new StringBuilder();

        for (FoodRating rating : ratings) {
            String feedback = rating.getFeedback();
            if (feedback != null && !feedback.isEmpty()) {
                String[] words = feedback.toLowerCase().split("\\W+");
                for (String word : words) {
                    if (!word.isBlank() && word.length() > 2) {
                        wordFrequency.put(word, wordFrequency.getOrDefault(word, 0) + 1);
                    }
                }
            }
        }

        List<String> topWords = wordFrequency.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        combinedFeedback.append("Common feedback: ");
        for (String word : topWords) {
            if (combinedFeedback.length() + word.length() + 1 > 180) break;
            combinedFeedback.append(word).append(" ");
        }

        return combinedFeedback.toString().trim();
    }
}