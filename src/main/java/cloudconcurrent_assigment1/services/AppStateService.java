package cloudconcurrent_assigment1.services;

import org.springframework.stereotype.Component;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicBoolean;


@Component
public class AppStateService {

    private final Instant serverStartTime;
    
    private final AtomicBoolean shuttingDown = new AtomicBoolean(false);

    public AppStateService() { // constructor 
        this.serverStartTime = Instant.now(); // setting server start time
    }

    public Instant getServerStartTime() { // get methods 
        return serverStartTime;
    }
    
    public AtomicBoolean getShuttingDown() {
    	return shuttingDown;
    }
}