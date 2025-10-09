package indie.services.moduloEventos;

import indie.models.moduloEventos.ArchivoEvento;
import indie.models.moduloEventos.Evento;
import indie.repositories.moduloEventos.ArchivoEventoRepository;
import indie.repositories.moduloEventos.EventoRepository;
import indie.services.BaseServiceImpl;
import indie.services.storage.IStorageService;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class ArchivoEventoServiceImpl extends BaseServiceImpl<ArchivoEvento, String> implements ArchivoEventoService {

    private final ArchivoEventoRepository archivoEventoRepository;
    private final EventoRepository eventoRepository;
    private final IStorageService storageService;

    public ArchivoEventoServiceImpl(
            ArchivoEventoRepository archivoEventoRepository,
            EventoRepository eventoRepository,
            IStorageService storageService) {
        super(archivoEventoRepository);
        this.archivoEventoRepository = archivoEventoRepository;
        this.eventoRepository = eventoRepository;
        this.storageService = storageService;
    }
    
    @Override
    public void init() {
        storageService.init();
    }

    @Override
    public ArchivoEvento uploadImagen(String eventoId, MultipartFile file) throws IOException {
        // Get the event
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new IllegalArgumentException("Evento not found"));

        // Store the file
        String filename = generateFilename(eventoId, file.getOriginalFilename());
        String url = storageService.store(file, filename);

        // Create the ArchivoEvento
        ArchivoEvento archivoEvento = new ArchivoEvento();
        archivoEvento.setIdEvento(evento);
        archivoEvento.setFechaAltaArchivoEvento(new Date()); // Current date
        archivoEvento.setFechaBajaArchivoEvento(new Date(LocalDateTime.now().plusYears(100).toLocalDate().toEpochDay())); // Far future
        archivoEvento.setTipoArchivoEvento(getFileType(file));
        archivoEvento.setUrlArchivoEvento(url);

        // Save and return
        return archivoEventoRepository.save(archivoEvento);
    }

    @Override
    public List<ArchivoEvento> uploadImagenes(String eventoId, List<MultipartFile> files) throws IOException {
        List<ArchivoEvento> result = new ArrayList<>();
        
        for (MultipartFile file : files) {
            result.add(uploadImagen(eventoId, file));
        }
        
        return result;
    }

    @Override
    public boolean deleteImagen(String archivoId) {
        // Get the ArchivoEvento
        ArchivoEvento archivoEvento = archivoEventoRepository.findById(archivoId)
                .orElseThrow(() -> new IllegalArgumentException("ArchivoEvento not found"));

        // Get the filename from the URL
        String url = archivoEvento.getUrlArchivoEvento();
        String filename = url.substring(url.lastIndexOf('/') + 1);

        // Delete the file
        boolean deleted = storageService.delete(filename);
        
        // If deletion was successful, delete the ArchivoEvento
        if (deleted) {
            archivoEventoRepository.delete(archivoEvento);
        }
        
        return deleted;
    }
    
    @Override
    public List<ArchivoEvento> getImagenesEvento(String eventoId) {
        // Verify event exists
        eventoRepository.findById(eventoId)
                .orElseThrow(() -> new IllegalArgumentException("Evento not found"));
                
        // Use repository method to find all ArchivoEvento objects for this event
        return archivoEventoRepository.findByEventoId(eventoId);
    }

    /**
     * Generate a unique filename for the file
     * @param eventoId ID of the event
     * @param originalFilename Original filename
     * @return Generated filename
     */
    private String generateFilename(String eventoId, String originalFilename) {
        String extension = "";
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex >= 0) {
            extension = originalFilename.substring(dotIndex);
        }
        
        return "evento_" + eventoId + "_" + System.currentTimeMillis() + extension;
    }
    
    /**
     * Get the type of the file
     * @param file MultipartFile to check
     * @return Type of the file (image/png, image/jpeg, etc.)
     */
    private String getFileType(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null ? contentType : "application/octet-stream";
    }
    
    @Override
    public List<ArchivoEvento> copyImagenes(String sourceEventoId, String targetEventoId) {
        // Verify both events exist
        eventoRepository.findById(sourceEventoId)
                .orElseThrow(() -> new IllegalArgumentException("Source evento not found"));
        
        eventoRepository.findById(targetEventoId)
                .orElseThrow(() -> new IllegalArgumentException("Target evento not found"));
                
        // Get all images for the source event
        List<ArchivoEvento> sourceImages = archivoEventoRepository.findByEventoId(sourceEventoId);
        
        // Create new ArchivoEvento objects for the target event
        List<ArchivoEvento> copiedImages = new java.util.ArrayList<>();
        
        for (ArchivoEvento sourceImage : sourceImages) {
            ArchivoEvento newImage = new ArchivoEvento();
            
            // Set the target event
            newImage.setIdEvento(eventoRepository.findById(targetEventoId).get());
            
            // Copy metadata
            newImage.setFechaAltaArchivoEvento(new java.util.Date());
            newImage.setFechaBajaArchivoEvento(sourceImage.getFechaBajaArchivoEvento());
            newImage.setTipoArchivoEvento(sourceImage.getTipoArchivoEvento());
            
            // Use the same URL - we're not physically copying the file,
            // just creating a new reference to it
            newImage.setUrlArchivoEvento(sourceImage.getUrlArchivoEvento());
            
            // Save the new image
            copiedImages.add(archivoEventoRepository.save(newImage));
        }
        
        return copiedImages;
    }
}
