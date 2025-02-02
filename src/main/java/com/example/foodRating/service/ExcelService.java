package com.example.foodRating.service;

import com.example.foodRating.entity.MealSummary;
import com.example.foodRating.entity.StudentRating;
import com.example.foodRating.repository.MealSummaryRepository;
import com.example.foodRating.repository.StudentRatingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExcelService {

    private static final Logger logger = LoggerFactory.getLogger(ExcelService.class);

    @Autowired
    private MealSummaryRepository mealSummaryRepository;

    @Autowired
    private StudentRatingRepository studentRatingRepository;

    public List<MealSummary> getMealSummaryData() {
        logger.info("Fetching meal summary data from the database.");
        return mealSummaryRepository.findAll();
    }

    public List<StudentRating> getStudentRatingsData() {
        logger.info("Fetching student ratings data from the database.");
        return studentRatingRepository.findAll();
    }
}