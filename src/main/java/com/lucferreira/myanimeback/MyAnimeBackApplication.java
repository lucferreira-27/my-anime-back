package com.lucferreira.myanimeback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MyAnimeBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyAnimeBackApplication.class, args);
	}

}
