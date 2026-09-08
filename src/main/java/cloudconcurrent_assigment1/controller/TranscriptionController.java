package cloudconcurrent_assigment1.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.client.RestClient;
import org.springframework.util.MultiValueMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;


@RestController
public class TranscriptionController {
	
	private final String openAiAPIKey;
	
	private final RestClient restClient;
	
	
	public TranscriptionController(@Value("${OPENAI_API_KEY}") String openAiAPIKey) {
		this.openAiAPIKey = openAiAPIKey;
		this.restClient = RestClient.create();
	}
	
	@PostMapping("/api/transcribe") // post method for getting the audio 
	public String transcribe(@RequestParam("audio") MultipartFile audio) throws IOException {
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
		
		System.out.println("OpenAI raw response: " + rawResponse);
		
		return rawResponse;
	}
	
	

}
