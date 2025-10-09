package indie.services.moduloEventos;

import indie.models.moduloEventos.ArchivoEvento;
import indie.services.BaseService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ArchivoEventoService extends BaseService<ArchivoEvento, String> {
    
    /**
     * Initialize the storage service
     */
    void init();
    
    /**
     * Upload an image for an event
     * @param eventoId ID of the event
     * @param file Image file to upload
     * @return The created ArchivoEvento
     * @throws IOException If the file cannot be stored
     */
    ArchivoEvento uploadImagen(String eventoId, MultipartFile file) throws IOException;
    
    /**
     * Upload multiple images for an event
     * @param eventoId ID of the event
     * @param files Image files to upload
     * @return List of created ArchivoEvento objects
     * @throws IOException If any file cannot be stored
     */
    List<ArchivoEvento> uploadImagenes(String eventoId, List<MultipartFile> files) throws IOException;
    
    /**
     * Delete an image
     * @param archivoId ID of the ArchivoEvento
     * @return true if the deletion was successful
     */
    boolean deleteImagen(String archivoId);
    
    /**
     * Get images for an event
     * @param eventoId ID of the event
     * @return List of ArchivoEvento objects
     */
    List<ArchivoEvento> getImagenesEvento(String eventoId);
    
    /**
     * Copy images from one event to another
     * @param sourceEventoId ID of the source event
     * @param targetEventoId ID of the target event
     * @return List of created ArchivoEvento objects
     */
    List<ArchivoEvento> copyImagenes(String sourceEventoId, String targetEventoId);
}
