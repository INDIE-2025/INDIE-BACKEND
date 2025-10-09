package indie.controllers.moduloEventos;

import indie.controllers.BaseController;
import indie.dtos.moduloEventos.ImagenEventoDTO;
import indie.models.moduloEventos.ArchivoEvento;
import indie.services.moduloEventos.ArchivoEventoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/archivoEvento")
@CrossOrigin(origins = "*")
public class ArchivoEventoController extends BaseController<ArchivoEvento, String> {

    private final ArchivoEventoService archivoEventoService;

    public ArchivoEventoController(ArchivoEventoService archivoEventoService) {
        super(archivoEventoService);
        this.archivoEventoService = archivoEventoService;
    }

    /**
     * Upload a single image for an event
     * @param eventoId ID of the event
     * @param file Image file to upload
     * @return The created ImagenEventoDTO with HTTP 201 status
     */
    @PostMapping("/evento/{eventoId}/upload")
    public ResponseEntity<ImagenEventoDTO> uploadImagen(
            @PathVariable(name = "eventoId") String eventoId,
            @RequestParam(name = "file") MultipartFile file) {
        try {
            ArchivoEvento archivo = archivoEventoService.uploadImagen(eventoId, file);
            ImagenEventoDTO dto = convertToDTO(archivo);
            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Convert ArchivoEvento to ImagenEventoDTO
     */
    private ImagenEventoDTO convertToDTO(ArchivoEvento archivo) {
        return new ImagenEventoDTO(
                archivo.getId(),
                archivo.getUrlArchivoEvento(),
                archivo.getTipoArchivoEvento()
        );
    }

    /**
     * Upload multiple images for an event
     * @param eventoId ID of the event
     * @param files Image files to upload
     * @return The list of created ImagenEventoDTO objects with HTTP 201 status
     */
    @PostMapping("/evento/{eventoId}/uploads")
    public ResponseEntity<List<ImagenEventoDTO>> uploadImagenes(
            @PathVariable(name = "eventoId") String eventoId,
            @RequestParam(name = "files") List<MultipartFile> files) {
        try {
            List<ArchivoEvento> archivos = archivoEventoService.uploadImagenes(eventoId, files);
            List<ImagenEventoDTO> dtos = archivos.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(dtos, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get all images for an event
     * @param eventoId ID of the event
     * @return List of ImagenEventoDTO objects with HTTP 200 status
     */
    @GetMapping("/evento/{eventoId}")
    public ResponseEntity<List<ImagenEventoDTO>> getImagenesEvento(@PathVariable(name = "eventoId") String eventoId) {
        try {
            List<ArchivoEvento> archivos = archivoEventoService.getImagenesEvento(eventoId);
            List<ImagenEventoDTO> dtos = archivos.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(dtos, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Delete an image
     * @param archivoId ID of the ArchivoEvento
     * @return HTTP 204 if successful, HTTP 404 if not found
     */
    @DeleteMapping("/{archivoId}")
    @Override
    public ResponseEntity<Void> delete(@PathVariable(name = "archivoId") String archivoId) {
        try {
            boolean deleted = archivoEventoService.deleteImagen(archivoId);
            if (deleted) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    /**
     * Copy images from one event to another
     * @param sourceEventoId ID of the source event
     * @param targetEventoId ID of the target event
     * @return List of ImagenEventoDTO objects with HTTP 201 status
     */
    @PostMapping("/copy/{sourceEventoId}/to/{targetEventoId}")
    public ResponseEntity<List<ImagenEventoDTO>> copyImagenes(
            @PathVariable(name = "sourceEventoId") String sourceEventoId,
            @PathVariable(name = "targetEventoId") String targetEventoId) {
        try {
            List<ArchivoEvento> copiedImages = archivoEventoService.copyImagenes(sourceEventoId, targetEventoId);
            List<ImagenEventoDTO> dtos = copiedImages.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(dtos, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
