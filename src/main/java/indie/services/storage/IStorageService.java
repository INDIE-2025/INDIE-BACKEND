package indie.services.storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * Interface for managing storage operations for files
 */
public interface IStorageService {
    
    /**
     * Initialize the storage (create directories, etc.)
     */
    void init();

    /**
     * Store a file with the given filename
     * @param file MultipartFile to store
     * @param filename Name to use for the file
     * @return The URL or path to access the file
     */
    String store(MultipartFile file, String filename) throws IOException;

    /**
     * Get all stored files
     * @return Stream of file paths
     */
    Stream<Path> loadAll();

    /**
     * Load a file as a Path
     * @param filename Name of the file
     * @return Path to the file
     */
    Path load(String filename);

    /**
     * Delete a file
     * @param filename Name of the file to delete
     * @return true if deletion was successful
     */
    boolean delete(String filename);
}