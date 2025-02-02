package com.example.foodRating;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.example.foodRating")
public class FoodRatingApplication {

	public static void main(String[] args) {
		SpringApplication.run(FoodRatingApplication.class, args);
	}

}
