package cloudconcurrent_assigment1.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


@RestController
public class TranscriptionController {
	
	private final String openAiAPIKey;
	
	
	public TranscriptionController(@Value("${OPENAI_API_KEY") String openAiAPIKey) {
		this.openAiAPIKey = openAiAPIKey;
	}
	
	@PostMapping("/api/transcribe") // post method for getting the audio 
	public String transcribe(@RequestParam("audio") MultipartFile audio) {
		System.out.println("Received file: " + audio.getOriginalFilename() + ", size: " + audio.getSize() + " bytes");
		return "received";
	}

}
