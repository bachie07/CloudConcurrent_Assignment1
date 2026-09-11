package cloudconcurrent_assigment1.controller;

import cloudconcurrent_assigment1.services.AppStateService;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.Instant;
import java.time.Duration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;





@RestController
public class AdminController {
	
	private final AppStateService appStateService;
	private final ApplicationContext applicationContext;

			
	private double calculateUpTimeSeconds(Instant start, Instant now) { // calculation duration between server start and current time
		
		Duration duration = Duration.between(start, now);
		
		double seconds = duration.toSeconds() + (duration.toNanosPart() / 1_000_000_000.0);
		
		return seconds;
		
	}
	
	public AdminController(AppStateService appStateService, ApplicationContext applicationContext ) { // constructor
		this.appStateService = appStateService;
		this.applicationContext = applicationContext;
	}

	
	@GetMapping("/api/v1/admin/uptime") // uptime API method
	
	public Map<String, Object> serviceReponse(){
		
		Instant now = Instant.now(); // get current time
		
		return Map.of( // required body value
				
				"utcServerStart", appStateService.getServerStartTime(),
				"utcNow", now,
				"serverUptimeSeconds", calculateUpTimeSeconds(appStateService.getServerStartTime(), now)
				
		);
				
			
	}
	
	
	
	@PostMapping("/api/v1/admin/shutdown") // shutdown API
	public ResponseEntity<Map<String, Object>> shutdownService(){
		
		if(appStateService.getShuttingDown().compareAndSet(false, true)) { // if server is on, set shuttingDown to true
			
			Map<String, Object> body = Map.of( // return message
					
					"message", "Graceful shutdown requested"
					
					);
			
			
			new Thread(() -> { // new thread handling shutdown
				
				try {
					Thread.sleep(500); // standard delay time ( safeguard ) 
					
				} catch(InterruptedException e){
					
					Thread.currentThread().interrupt();
					
				}
				SpringApplication.exit(applicationContext, () -> 0); // // shutdown command
				
			}).start();
			
			
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(body); // original thread return body status
			
					
		}
		
		else { // if shutdown already happen 
			
			Map<String, Object> body = Map.of( // return conflict error body 
					
					"timestamp", Instant.now().toString(),
					"status", HttpStatus.CONFLICT.value(),
					"error", "Conflict",
					"message", "Graceful shutdown is already in progress",
					"path", "/api/v1/admin/shutdown"
					
			);
			
			return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
			
			
		}
		

		
	}
	
	
	

}
