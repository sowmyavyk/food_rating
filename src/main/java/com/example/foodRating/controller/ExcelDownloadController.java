package com.example.foodRating.controller;

import com.example.foodRating.service.ExcelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;

@RestController
@RequestMapping("/excel")
public class ExcelDownloadController {

    private static final Logger logger = LoggerFactory.getLogger(ExcelDownloadController.class);

    @Autowired
    private ExcelService excelService;

    @GetMapping("/download/meal-summary")
    public ResponseEntity<?> downloadMealSummary() {
        logger.info("Request received to download meal summary Excel.");
        try {
            ByteArrayInputStream stream = excelService.generateMealSummaryExcel();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=meal_summary.xlsx");

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                    .body(new InputStreamResource(stream));
        } catch (Exception e) {
            logger.error("Error occurred while generating meal summary Excel: ", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error generating Excel file: " + e.getMessage());
        }
    }

    @GetMapping("/download/student-ratings")
    public ResponseEntity<?> downloadStudentRatings() {
        logger.info("Request received to download student ratings Excel.");
        try {
            ByteArrayInputStream stream = excelService.generateStudentRatingsExcel();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=student_ratings.xlsx");

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                    .body(new InputStreamResource(stream));
        } catch (Exception e) {
            logger.error("Error occurred while generating student ratings Excel: ", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error generating Excel file: " + e.getMessage());
        }
    }
}