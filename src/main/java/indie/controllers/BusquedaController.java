package indie.controllers;

import indie.dtos.busqueda.ResultadoBusquedaDTO;
import indie.services.BusquedaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/busqueda")
public class BusquedaController {

    @Autowired
    private BusquedaService busquedaService;

    
    @GetMapping
    public ResponseEntity<ResultadoBusquedaDTO> busqueda(
            @RequestParam("q") String termino,
            @RequestParam(value = "tipo", defaultValue = "all") String tipo,
            @RequestParam(value = "limite", defaultValue = "10") int limite,
            @RequestParam(value = "ordenarPor", defaultValue = "relevancia") String ordenarPor) {
        
        ResultadoBusquedaDTO resultados = busquedaService.busquedaUnificada(termino, tipo, limite, ordenarPor);
        return ResponseEntity.ok(resultados);
    }

    
    @GetMapping("/usuarios")
    public ResponseEntity<ResultadoBusquedaDTO> buscarUsuarios(
            @RequestParam("q") String termino,
            @RequestParam(value = "limite", defaultValue = "10") int limite) {
        
        ResultadoBusquedaDTO resultados = busquedaService.buscarUsuarios(termino, limite);
        return ResponseEntity.ok(resultados);
    }

    
    @GetMapping("/eventos")
    public ResponseEntity<ResultadoBusquedaDTO> buscarEventos(
            @RequestParam("q") String termino,
            @RequestParam(value = "limite", defaultValue = "10") int limite) {
        
        ResultadoBusquedaDTO resultados = busquedaService.buscarEventos(termino, limite);
        return ResponseEntity.ok(resultados);
    }
}