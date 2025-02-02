package com.example.foodRating.service;

import com.example.foodRating.entity.MealSummary;
import com.example.foodRating.entity.StudentRating;
import com.example.foodRating.repository.MealSummaryRepository;
import com.example.foodRating.repository.StudentRatingRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class ExcelService {

    private static final Logger logger = LoggerFactory.getLogger(ExcelService.class);

    @Autowired
    private MealSummaryRepository mealSummaryRepository;

    @Autowired
    private StudentRatingRepository studentRatingRepository;

    public ByteArrayInputStream generateMealSummaryExcel() {
        logger.info("Starting meal summary Excel generation...");
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Meal Summary");

            // Create header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Date");
            headerRow.createCell(2).setCellValue("Meal Type");
            headerRow.createCell(3).setCellValue("Total Students Rated");
            headerRow.createCell(4).setCellValue("Average Rating");
            headerRow.createCell(5).setCellValue("Summarized Feedback");

            // Add data rows
            List<MealSummary> mealSummaries = mealSummaryRepository.findAll();
            logger.info("Fetched {} meal summaries from the database.", mealSummaries.size());

            int rowNum = 1;
            for (MealSummary summary : mealSummaries) {
                Row row = sheet.createRow(rowNum++);

                // Handle ID
                row.createCell(0).setCellValue(summary.getId() != null ? summary.getId() : 0);

                // Handle Date with null check
                Cell dateCell = row.createCell(1);
                if (summary.getDate() != null) {
                    dateCell.setCellValue(summary.getDate().toString());
                } else {
                    dateCell.setCellValue("N/A");
                }

                // Handle Meal Type with null check
                row.createCell(2).setCellValue(summary.getMealType() != null ? summary.getMealType() : "N/A");

                // Handle Total Students
                row.createCell(3).setCellValue(summary.getTotalStudentsRated());

                // Handle Average Rating
                row.createCell(4).setCellValue(summary.getAverageRating());

                // Handle Feedback with null check
                row.createCell(5).setCellValue(summary.getSummarizedFeedback() != null ?
                        summary.getSummarizedFeedback() : "No feedback available");
            }

            // Autosize columns
            for (int i = 0; i < 6; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            logger.info("Meal summary Excel file generated successfully.");
            return new ByteArrayInputStream(outputStream.toByteArray());
        } catch (Exception e) {
            logger.error("Error generating meal summary Excel file: ", e);
            throw new RuntimeException("Failed to generate Excel file: " + e.getMessage(), e);
        }
    }

    public ByteArrayInputStream generateStudentRatingsExcel() {
        logger.info("Starting student ratings Excel generation...");
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Student Ratings");

            // Create header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Roll Number");
            headerRow.createCell(2).setCellValue("Date");
            headerRow.createCell(3).setCellValue("Meal Type");
            headerRow.createCell(4).setCellValue("Rating");
            headerRow.createCell(5).setCellValue("Feedback");

            // Add data rows
            List<StudentRating> studentRatings = studentRatingRepository.findAll();
            logger.info("Fetched {} student ratings from the database.", studentRatings.size());

            int rowNum = 1;
            for (StudentRating rating : studentRatings) {
                Row row = sheet.createRow(rowNum++);

                // Handle ID
                row.createCell(0).setCellValue(rating.getId() != null ? rating.getId() : 0);

                // Handle Roll Number with null check
                row.createCell(1).setCellValue(rating.getRollNumber() != null ? rating.getRollNumber() : "N/A");

                // Handle Date with null check
                Cell dateCell = row.createCell(2);
                if (rating.getDate() != null) {
                    dateCell.setCellValue(rating.getDate().toString());
                } else {
                    dateCell.setCellValue("N/A");
                }

                // Handle Meal Type with null check
                row.createCell(3).setCellValue(rating.getMealType() != null ? rating.getMealType() : "N/A");

                // Handle Rating
                row.createCell(4).setCellValue(rating.getRating());

                // Handle Feedback with null check
                row.createCell(5).setCellValue(rating.getFeedback() != null ? rating.getFeedback() : "No feedback");
            }

            // Autosize columns
            for (int i = 0; i < 6; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            logger.info("Student ratings Excel file generated successfully.");
            return new ByteArrayInputStream(outputStream.toByteArray());
        } catch (Exception e) {
            logger.error("Error generating student ratings Excel file: ", e);
            throw new RuntimeException("Failed to generate Excel file: " + e.getMessage(), e);
        }
    }
}