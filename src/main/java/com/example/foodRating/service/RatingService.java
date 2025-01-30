package com.example.foodRating.service;

import com.example.foodRating.entity.MealSummary;
import com.example.foodRating.entity.StudentRating;
import com.example.foodRating.repository.MealSummaryRepository;
import com.example.foodRating.repository.StudentRatingRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

@Service
public class RatingService {

    @Autowired
    private StudentRatingRepository studentRatingRepository;

    @Autowired
    private MealSummaryRepository mealSummaryRepository;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Hugging Face API details
    private static final String HUGGING_FACE_API_URL = "https://api-inference.huggingface.co/models/facebook/bart-large-cnn";
    private static final String HUGGING_FACE_API_KEY = "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"; // Replace with your API key

    public void submitRating(StudentRating rating) {
        studentRatingRepository.save(rating);
        updateMealSummary(rating.getDate(), rating.getMealType());
    }

    private void updateMealSummary(LocalDate date, String mealType) {
        List<StudentRating> ratings = studentRatingRepository.findByDateAndMealType(date, mealType);
        int totalStudents = ratings.size();
        double averageRating = ratings.stream().mapToInt(StudentRating::getRating).average().orElse(0.0);
        String summarizedFeedback = summarizeFeedback(ratings);

        MealSummary summary = mealSummaryRepository.findByDateAndMealType(date, mealType)
                .orElse(new MealSummary());
        summary.setDate(date);
        summary.setMealType(mealType);
        summary.setTotalStudentsRated(totalStudents);
        summary.setAverageRating(averageRating);
        summary.setSummarizedFeedback(summarizedFeedback);

        mealSummaryRepository.save(summary);
    }

    private String summarizeFeedback(List<StudentRating> ratings) {
        // Combine all feedback into a single string
        String allFeedback = ratings.stream()
                .map(StudentRating::getFeedback)
                .reduce("", (a, b) -> a + " " + b)
                .trim();

        // Summarize the feedback using Hugging Face API
        return summarizeTextWithHuggingFace(allFeedback);
    }

    private String summarizeTextWithHuggingFace(String text) {
        try {
            // Prepare the request payload
            String requestBody = String.format("{\"inputs\": \"%s\"}", text);
    
            // Set up headers with the API key
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + HUGGING_FACE_API_KEY);
    
            // Create the HTTP entity with headers and body
            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
    
            // Send the request to Hugging Face API
            ResponseEntity<String> response = restTemplate.exchange(
                    HUGGING_FACE_API_URL,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );
    
            // Parse the response to extract the summarized text
            JsonNode responseJson = objectMapper.readTree(response.getBody());
            if (responseJson.isArray() && responseJson.size() > 0) {
                JsonNode summaryNode = responseJson.get(0).get("summary_text");
                if (summaryNode != null) {
                    String summarizedText = summaryNode.asText();
    
                    // Ensure the summarized feedback is no more than 180 characters
                    if (summarizedText.length() > 180) {
                        summarizedText = summarizedText.substring(0, 180).trim();
                    }
    
                    return summarizedText;
                }
            }
    
            return "Unable to summarize feedback.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error summarizing feedback.";
        }
    }

    public List<MealSummary> getTodaysSummary() {
        return mealSummaryRepository.findByDate(LocalDate.now());
    }
}