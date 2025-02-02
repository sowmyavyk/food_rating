package com.example.foodRating.controller;

import com.example.foodRating.entity.MealSummary;
import com.example.foodRating.entity.StudentRating;
import com.example.foodRating.service.ExcelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DataController {

    private static final Logger logger = LoggerFactory.getLogger(DataController.class);

    @Autowired
    private ExcelService excelService;

    @GetMapping("/meal-summary")
    public ResponseEntity<?> getMealSummary() {
        logger.info("Request received to fetch meal summary data.");
        try {
            List<MealSummary> mealSummaries = excelService.getMealSummaryData();
            return ResponseEntity.ok(mealSummaries);
        } catch (Exception e) {
            logger.error("Error occurred while fetching meal summary data: ", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching meal summary data: " + e.getMessage());
        }
    }

    @GetMapping("/student-ratings")
    public ResponseEntity<?> getStudentRatings() {
        logger.info("Request received to fetch student ratings data.");
        try {
            List<StudentRating> studentRatings = excelService.getStudentRatingsData();
            return ResponseEntity.ok(studentRatings);
        } catch (Exception e) {
            logger.error("Error occurred while fetching student ratings data: ", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching student ratings data: " + e.getMessage());
        }
    }
}