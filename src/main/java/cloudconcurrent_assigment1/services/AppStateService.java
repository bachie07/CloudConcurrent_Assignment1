package cloudconcurrent_assigment1.services;

import org.springframework.stereotype.Component;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;


@Component
public class AppStateService {

    private final Instant serverStartTime;
    
    private final AtomicBoolean shuttingDown = new AtomicBoolean(false);
    
    private final AtomicLong inputTokens = new AtomicLong(0);
    
    private final AtomicLong outputTokens = new AtomicLong(0);
    

    public AppStateService() { // constructor 
        this.serverStartTime = Instant.now(); // setting server start time
        
    }

    public Instant getServerStartTime() { // get methods 
        return serverStartTime;
    }
    
    public AtomicBoolean getShuttingDown() {
    	return shuttingDown;
    }
    
    public void addInputTokens(long tokens) {
    	
    	inputTokens.addAndGet(tokens);
    	
    }
    
    public void addOutputTokens(long tokens) {
    	
    	outputTokens.addAndGet(tokens);
    	
    }
    
    public long getInputTokens() {
    	
    	return inputTokens.get();
    }
    
    public long getOutputTokens() {
    	
    	return outputTokens.get();
    }
}