package com.example.foodRating.controller;

import com.example.foodRating.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;

@RestController
@RequestMapping("/excel")
public class ExcelDownloadController {

    @Autowired
    private ExcelService excelService;

    @GetMapping("/download/meal-summary")
    public ResponseEntity<InputStreamResource> downloadMealSummary() {
        ByteArrayInputStream stream = excelService.generateMealSummaryExcel();
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=meal_summary.xlsx");
        
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(new InputStreamResource(stream));
    }

    @GetMapping("/download/student-ratings")
    public ResponseEntity<InputStreamResource> downloadStudentRatings() {
        ByteArrayInputStream stream = excelService.generateStudentRatingsExcel();
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=student_ratings.xlsx");
        
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(new InputStreamResource(stream));
    }
}
