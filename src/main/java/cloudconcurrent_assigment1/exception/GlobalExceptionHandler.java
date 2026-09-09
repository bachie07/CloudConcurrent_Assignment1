package cloudconcurrent_assigment1.exception;


import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, Object>> handleException(Exception ex, HttpServletRequest request){
		
		Map<String, Object> body = Map.of(
				
				"timestamp", Instant.now().toString(),
				"status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"error", "Internal Server Error",
				"message", ex.getMessage(),
				"path", request.getRequestURI()
				
				);
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
				
	} 

}
