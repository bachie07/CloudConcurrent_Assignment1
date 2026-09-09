package cloudconcurrent_assigment1.exception;


import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, Object>> handleException(Exception ex, HttpServletRequest request){
		
		
		Map<String, Object> body = Map.of(
				
				"timestamp", Instant.now().toString(),
				"status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"error", "Internal Server Error",
				"message", ex.getMessage(),
				"path", request.getRequestURI()
				
				);
		
		logger.error("Unhandled exception on {}: {}", request.getRequestURI(), ex.getMessage());
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
				
	} 

}
