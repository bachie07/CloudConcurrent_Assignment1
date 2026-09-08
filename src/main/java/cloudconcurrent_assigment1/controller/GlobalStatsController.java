package cloudconcurrent_assigment1.controller;


import cloudconcurrent_assigment1.services.AppStateService;

import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class GlobalStatsController {
	
	
	private final AppStateService appStateService;

	
	public GlobalStatsController(AppStateService appStateService) {
		
		this.appStateService = appStateService;
	}
	
	@GetMapping("/api/v1/global/stats")
	public Map<String, Object> getGlobalStats(){
		
		return Map.of(                                    
				
				"inputTokens", appStateService.getInputTokens(),
				"outputTokens", appStateService.getOutputTokens()
				
				);
	}

}
