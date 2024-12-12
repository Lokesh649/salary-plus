package com.salaryplus.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobMarketRequest {
	@NotEmpty(message = "Prompt cannot be empty")
	private String prompt;
}
