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



@RestController
public class TranscriptionController {
	
	private final String openAiAPIKey;
	
	private final RestClient restClient;
	
	private final AppStateService appStateService;

	
	public TranscriptionController(@Value("${OPENAI_API_KEY}") String openAiAPIKey, AppStateService appStateService) {
		this.openAiAPIKey = openAiAPIKey;
		this.restClient = RestClient.create();
		this.appStateService = appStateService;
	}
	
	@PostMapping("/api/transcribe") // post method for getting the audio 
	public Map<String, Object> transcribe(@RequestParam("audio") MultipartFile audio) throws IOException {
		System.out.println("Received file: " + audio.getOriginalFilename() + ", size: " + audio.getSize() + " bytes");
		
		ByteArrayResource audioResource = new ByteArrayResource(audio.getBytes()) {
			@Override
			public String getFilename() {
				return audio.getOriginalFilename();
			}
		};
		
		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("file", audioResource);
		body.add("model", "gpt-4o-mini-transcribe");
		
		String rawResponse = restClient.post()
				.uri("https://api.openai.com/v1/audio/transcriptions")
				.header("Authorization", "Bearer " + openAiAPIKey)
				.contentType(MediaType.MULTIPART_FORM_DATA)
				.body(body)
				.retrieve()
				.body(String.class);
						
		JsonNode json = JsonMapper.shared().readTree(rawResponse);
		String text = json.get("text").asString();

		JsonNode usage = json.get("usage");
		long inputTokens = usage.get("input_tokens").asLong();
		long outputTokens = usage.get("output_tokens").asLong();
		

		appStateService.addInputTokens(inputTokens);
		appStateService.addOutputTokens(outputTokens);
		
		
		return Map.of("text", text);
	}
	
	

}
