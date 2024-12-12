package com.salaryplus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Salary Plus",
				description = "Job Market Trends and Insights By AI ",
				version = "1.0"
				))
public class SalaryPlusServicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalaryPlusServicesApplication.class, args);
	}

}
