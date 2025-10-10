package indie.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Value("${storage.location:uploads}")
    private String storageLocation;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Map URL path /images/** to the physical file system location
        Path uploadDir = Paths.get(storageLocation);
        String uploadPath = uploadDir.toFile().getAbsolutePath();
        
        // Make sure the path ends with a directory separator
        if (!uploadPath.endsWith(System.getProperty("file.separator"))) {
            uploadPath += System.getProperty("file.separator");
        }
        
        // Register both paths to the same physical directory
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + uploadPath);
        
        // Add a specific path for each event ID pattern
        registry.addResourceHandler("/api/archivoEvento/evento/*/uploads/**")
                .addResourceLocations("file:" + uploadPath);
    }
}