package com.example.foodRating.controller;
import com.example.foodRating.entity.MealSummary;
import com.example.foodRating.entity.StudentRating;
import com.example.foodRating.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/ratings")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @PostMapping("/submit")
    public String submitRating(@RequestBody StudentRating rating) {
        rating.setDate(LocalDate.now());
        ratingService.submitRating(rating);
        return "Rating submitted successfully!";
    }

    @GetMapping("/summary")
    public List<MealSummary> getTodaysSummary() {
        return ratingService.getTodaysSummary();
    }
}