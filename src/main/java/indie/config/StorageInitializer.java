package indie.config;

import indie.services.storage.IStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StorageInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(StorageInitializer.class);
    
    private final IStorageService storageService;
    
    public StorageInitializer(IStorageService storageService) {
        this.storageService = storageService;
    }
    
    @Override
    public void run(String... args) throws Exception {
        try {
            log.info("Initializing storage service...");
            storageService.init();
        } catch (Exception e) {
            log.error("Could not initialize storage service", e);
        }
    }
}