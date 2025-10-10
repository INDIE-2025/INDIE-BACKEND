package indie.services.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

/**
 * Service for storing files locally
 */
@Service("localStorageService")
public class LocalStorageService implements IStorageService {

    private final Path rootLocation;
    private final String uploadUrl;

    public LocalStorageService(
            @Value("${storage.location:uploads}") String uploadDir,
            @Value("${storage.base-url:http://localhost:8080/images/}") String uploadUrl) {
        this.rootLocation = Paths.get(uploadDir);
        this.uploadUrl = uploadUrl;
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage location", e);
        }
    }

    @Override
    public String store(MultipartFile file, String filename) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Failed to store empty file");
        }
        
        String sanitizedFilename = sanitizeFilename(filename);
        
        // Ensure file doesn't already exist (append timestamp if it does)
        if (Files.exists(this.rootLocation.resolve(sanitizedFilename))) {
            String nameWithoutExtension = sanitizedFilename;
            String extension = "";
            int lastDotIndex = sanitizedFilename.lastIndexOf('.');
            if (lastDotIndex > 0) {
                nameWithoutExtension = sanitizedFilename.substring(0, lastDotIndex);
                extension = sanitizedFilename.substring(lastDotIndex);
            }
            sanitizedFilename = nameWithoutExtension + "_" + System.currentTimeMillis() + extension;
        }
        
        try {
            Files.copy(file.getInputStream(), 
                       this.rootLocation.resolve(sanitizedFilename),
                       StandardCopyOption.REPLACE_EXISTING);
            
            // Return the URL to access the file
            return uploadUrl + "/" + sanitizedFilename;
        } catch (IOException e) {
            throw new IOException("Failed to store file", e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.list(this.rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read stored files", e);
        }
    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public boolean delete(String filename) {
        try {
            Path file = load(filename);
            return Files.deleteIfExists(file);
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Sanitize the filename to avoid security issues
     * @param filename Original filename
     * @return Sanitized filename
     */
    private String sanitizeFilename(String filename) {
        // Replace any character that's not a-z, A-Z, 0-9, dot, underscore or dash with underscore
        String sanitized = filename.replaceAll("[^a-zA-Z0-9._-]", "_");
        return sanitized;
    }
}