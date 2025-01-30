package com.example.foodRating.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class MealSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private String mealType; // Breakfast, Lunch, Snacks, Dinner
    private int totalStudentsRated;
    private double averageRating;
    private String summarizedFeedback;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public String getMealType() { return mealType; }
    public void setMealType(String mealType) { this.mealType = mealType; }
    public int getTotalStudentsRated() { return totalStudentsRated; }
    public void setTotalStudentsRated(int totalStudentsRated) { this.totalStudentsRated = totalStudentsRated; }
    public double getAverageRating() { return averageRating; }
    public void setAverageRating(double averageRating) { this.averageRating = averageRating; }
    public String getSummarizedFeedback() { return summarizedFeedback; }
    public void setSummarizedFeedback(String summarizedFeedback) { this.summarizedFeedback = summarizedFeedback; }
}