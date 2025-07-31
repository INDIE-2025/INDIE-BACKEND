package indie.controllers;

import indie.models.Archivo;
import indie.services.ArchivoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/archivo")
public class ArchivoController extends BaseController<Archivo, String> {

    public ArchivoController(ArchivoService archivoService) {
        super(archivoService);
    }
}