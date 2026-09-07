package cloudconcurrent_assigment1.controller;

import cloudconcurrent_assigment1.services.AppStateService;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.Instant;
import java.time.Duration;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.atomic.AtomicBoolean;




@RestController
public class AdminController {
	
	private final AppStateService appStateService;
			
	private double calculateUpTimeSeconds(Instant start, Instant now) {
		
		Duration duration = Duration.between(start, now);
		
		double seconds = duration.toSeconds() + (duration.toNanosPart() / 1_000_000_000.0);
		
		return seconds;
		
	}
	
	public AdminController(AppStateService appStateService) {
		this.appStateService = appStateService;
	}

	
	@GetMapping("/api/v1/admin/uptime")
	
	public Map<String, Object> serviceReponse(){
		
		Instant now = Instant.now();
		
		return Map.of(
				
				"utcServerStart", appStateService.getServerStartTime(),
				"utcNow", now,
				"serverUptimeSeconds", calculateUpTimeSeconds(appStateService.getServerStartTime(), now)
				
		);
				
			
	}
	
	@PostMapping("/api/v1/admin/shutdown")
	public ResponseEntity<Map<String, Object>> shutdownService(){
		
		Map<String, Object> body = Map.of(
				
				"timestamp", Instant.now().toString(),
				"status", HttpStatus.CONFLICT.value(),
				"error", "Conflict",
				"message", "Graceful shutdown is already in progress"
				
		);
		
		return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
		
	}
	
	
	

}
