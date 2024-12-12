package com.salaryplus.service;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.salaryplus.dto.Contents;
import com.salaryplus.dto.PartClass;
import com.salaryplus.dto.TextClass;
import com.salaryplus.entity.JobTrend;
import com.salaryplus.entity.PositionTrend;
import com.salaryplus.repository.JobTrendRepository;
import com.salaryplus.repository.PositionTrendRepository;
import com.salaryplus.utility.GeminiRequest;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class JobMarketService {

	@Autowired
	PositionTrendRepository positionRepo;

	@Autowired
	JobTrendRepository jobRepo;

	@Autowired
	RestTemplate restTemplate;

	@Value("${gemini.api.base-url}")
	String GEMINI_API_ENDPOINT;
	@Value("${gemini.api.key}")
	String API_KEY;

	public String getResponse(String prompt) {
		String response = null;
		try {
			log.info("prompt:-" + prompt);
			List<PositionTrend> positionData = positionRepo.findAll();
			log.info("position data size:-" + positionData.size());
			List<JobTrend> jobData = jobRepo.findAll();

			// GeminiRequest request = new GeminiRequest();
			// request.setPrompt(prompt);

			// request.setDatasets(List.of(positionData, jobData));
TextClass text = new TextClass();
text.setText(prompt+":use provided data"+positionData+" "+jobData);
PartClass part = new PartClass();
part.setParts(List.of(text));
			Contents request = new Contents();
			request.setContents(List.of(part));

			Gson gson = new Gson();
			String requestJson = gson.toJson(request);

			// Set up the request headers
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setContentType(MediaType.APPLICATION_JSON);
			// httpHeaders.set("Authorization", "Bearer " + API_KEY); // Use Bearer token
			// for authentication

			// Create the request entity
			HttpEntity<String> entity = new HttpEntity<>(requestJson, httpHeaders);

			// Make the API call using RestTemplate
			ResponseEntity<String> responseEntity = restTemplate.exchange(GEMINI_API_ENDPOINT + API_KEY,
					HttpMethod.POST, entity, String.class);
JSONObject resp = new JSONObject(responseEntity.getBody());
			// Parse the response from Gemini API
			response = responseEntity.getBody();
		} catch (Exception e) {
			log.error(e.getMessage());
		}

		return response;

	}

}
