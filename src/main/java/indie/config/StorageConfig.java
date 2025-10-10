package indie.config;

import indie.services.storage.IStorageService;
import indie.services.storage.LocalStorageService;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class StorageConfig {

    @Bean
    @Primary
    public IStorageService storageService(
            @Qualifier("localStorageService") LocalStorageService localStorageService) {
        
        // Always use local storage
        return localStorageService;
    }
}