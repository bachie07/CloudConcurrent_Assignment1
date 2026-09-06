package cloudconcurrent_assigment1.services;

import org.springframework.stereotype.Component;
import java.time.Instant;

@Component
public class AppStateService {

    private final Instant serverStartTime;

    public AppStateService() {
        this.serverStartTime = Instant.now();
    }

    public Instant getServerStartTime() {
        return serverStartTime;
    }
}