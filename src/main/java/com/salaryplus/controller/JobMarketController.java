package com.salaryplus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.salaryplus.service.JobMarketService;

@RestController
@RequestMapping("/salary-plus")
 @CrossOrigin("*")
public class JobMarketController {

	@Autowired
	JobMarketService service;

	@GetMapping("/chat")
	public ResponseEntity<String> getResponse(@RequestParam String prompt) throws JsonMappingException, JsonProcessingException {
		return ResponseEntity.ok(service.getResponse(prompt));
	}

	@GetMapping("/")
	public String HealthCheck(){
		return "Application is up and running";
	}
}
