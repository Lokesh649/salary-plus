package com.salaryplus.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import jakarta.persistence.EntityManager;
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

	@Autowired
	EntityManager entityManager;

	@Value("${gemini.api.base-url}")
	String GEMINI_API_ENDPOINT;
	@Value("${gemini.api.key}")
	String API_KEY;

	public String getResponse(String prompt) throws JsonMappingException, JsonProcessingException {
		String response = null;
		try {
			log.info("prompt:-" + prompt);
			List<Object> datsets = getandExecuteQuery(prompt);
			List<PositionTrend> positionData = (List<PositionTrend>)datsets.get(1);
			log.info("position data size:-" + positionData.size());
			List<JobTrend> jobData = (List<JobTrend>)datsets.get(0);

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

		ObjectMapper objectMapper = new ObjectMapper();

            // Parse the JSON string
            JsonNode rootNode = objectMapper.readTree(response);

            // Navigate to the "text" key
            JsonNode textNode = rootNode
                .path("candidates") // Navigate to "candidates"
                .get(0) // Get the first element of the array
                .path("content") // Navigate to "content"
                .path("parts") // Navigate to "parts"
                .get(0) // Get the first element of the array
                .path("text"); // Get the "text" key

            // Extract and print the value
            if (!textNode.isMissingNode()) {
                // String textValue = textNode.asText().replace("`", "");
				 response = textNode.asText();
			}

		return response;

	}

	public List<Object> getandExecuteQuery(String prompt) {
        List<Object> dataSets = new ArrayList<>();
        //interact with gemini and get queries as stringarray
		StringBuilder query =  new StringBuilder();
		query.append(":I have two tables: jobtrend (with columns: id, role, company, location, experience, skills) and positiontrend (with columns: id, position, gender, location, education, experience, salary). Please provide two SQL queries that use SELECT * and filter the results using a WHERE clause. The filter criteria should be based on a prompt (the text before a colon). For example, if the prompt is 'Role: Software Engineer', the query should filter the jobtrend table to only include rows where the role column contains 'Software Engineer'. Please provide the queries as a string array, without any additional explanation.");
        
		TextClass text = new TextClass();
		text.setText(query.toString());
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

							String[] queries = queries(responseEntity.getBody());
							log.info("job:-"+queries[0]);
							log.info("position:-"+queries[1]);

		List<JobTrend> jobtrendList = entityManager.createNativeQuery(queries[0], JobTrend.class).getResultList();

        List<PositionTrend> positiontrendList = entityManager.createNativeQuery(queries[1], PositionTrend.class).getResultList();
        dataSets.add(jobtrendList);
        dataSets.add(positiontrendList);
        return dataSets;
    }

	public String[] queries(String result){
		try {
            // Create an ObjectMapper instance
            ObjectMapper objectMapper = new ObjectMapper();

            // Parse the JSON string
            JsonNode rootNode = objectMapper.readTree(result);

            // Navigate to the "text" key
            JsonNode textNode = rootNode
                .path("candidates") // Navigate to "candidates"
                .get(0) // Get the first element of the array
                .path("content") // Navigate to "content"
                .path("parts") // Navigate to "parts"
                .get(0) // Get the first element of the array
                .path("text"); // Get the "text" key

            // Extract and print the value
            if (!textNode.isMissingNode()) {
                // String textValue = textNode.asText().replace("`", "");
				String textValue = textNode.asText().replace("`", "").replace("sql", "").replace("json", "");
				String[] stringArray = objectMapper.readValue(textValue, String[].class);          
                log.info("Extracted Text: " + stringArray);
				return stringArray;
            } else {
                System.out.println("Text key not found!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		return null;
    }
	}


