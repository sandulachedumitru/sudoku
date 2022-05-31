package com.hardcodacii;

import com.hardcodacii.controller.MainController;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class SudokuApplication implements CommandLineRunner {
	private final MainController mainController;

	public static void main(String[] args) {
		SpringApplication.run(SudokuApplication.class, args);
	}

	@Override
	public void run(String... args) {
		mainController.start(args);
	}
}
