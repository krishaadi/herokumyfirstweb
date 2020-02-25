package com.deloitte.HerokuUseCaseSample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class HerokuUseCaseSampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(HerokuUseCaseSampleApplication.class, args);
	}
	
}
//Adkrishna : Added to test Simple Spring boot 
@RestController
class HerokuController{
	@GetMapping("/myWeb")
	String hello()
	{
		return "My First Web Appp";
	}
}
//Close Simple App

