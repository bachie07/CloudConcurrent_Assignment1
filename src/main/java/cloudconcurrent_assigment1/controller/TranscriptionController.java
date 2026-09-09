package cloudconcurrent_assigment1.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import cloudconcurrent_assigment1.services.AppStateService;

import org.springframework.web.client.RestClient;
import org.springframework.util.MultiValueMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


// core feature: receive audio, forward to openAI and return transcribed text

@RestController
public class TranscriptionController {
	
	private final String openAiAPIKey;
	
	private final RestClient restClient;
	
	private final AppStateService appStateService;
	
	private static final Logger logger = LoggerFactory.getLogger(TranscriptionController.class);

	
	//openAIAPi read from env on startup only - not hardcoded or logged
	public TranscriptionController(@Value("${OPENAI_API_KEY}") String openAiAPIKey, AppStateService appStateService) {
		this.openAiAPIKey = openAiAPIKey;
		this.restClient = RestClient.create();
		this.appStateService = appStateService;
	}
	
	@PostMapping("/api/transcribe") // post method for getting the audio 
	public Map<String, Object> transcribe(@RequestParam("audio") MultipartFile audio) throws IOException {
		
		logger.info("Received file: {}, size: {} bytes", audio.getOriginalFilename(), audio.getSize());
		
		//OpenAI detects audio format from the filename extension, not raw bytes
		// so the original filename gets reattqached here
		ByteArrayResource audioResource = new ByteArrayResource(audio.getBytes()) {
			@Override
			public String getFilename() {
				return audio.getOriginalFilename();
			}
		};
		
		//MultiValueMap is the preferred multipart body for blocking restClient
		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("file", audioResource);
		body.add("model", "gpt-4o-mini-transcribe");
		
		
		//Blocking call - pause this thread until OpenAI responds
		String rawResponse = restClient.post()
				.uri("https://api.openai.com/v1/audio/transcriptions")
				.header("Authorization", "Bearer " + openAiAPIKey)
				.contentType(MediaType.MULTIPART_FORM_DATA)
				.body(body)
				.retrieve()
				.body(String.class);
		
		
		//Extract what we need rather then getting whole OpenAI raw response
						
		JsonNode json = JsonMapper.shared().readTree(rawResponse);
		String text = json.get("text").asString();

		JsonNode usage = json.get("usage");
		long inputTokens = usage.get("input_tokens").asLong();
		long outputTokens = usage.get("output_tokens").asLong();
		

		appStateService.addInputTokens(inputTokens);
		appStateService.addOutputTokens(outputTokens);
		
		logger.info("Transcription successful, {} input tokens, {} output tokens", inputTokens, outputTokens);
		
		return Map.of("text", text);
	}
	
	

}
