package cloudconcurrent_assigment1;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TranscriptionController {
	
	private final String openAiAPIKey;
	
	
	public TranscriptionController(@Value("${OPENAI_API_KEY") String openAiAPIKey) {
		this.openAiAPIKey = openAiAPIKey;
	}

}
