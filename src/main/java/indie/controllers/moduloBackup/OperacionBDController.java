package indie.controllers.moduloBackup;

import indie.controllers.BaseController;
import indie.models.moduloBackUp.OperacionBD;
import indie.services.moduloBackup.OperacionBDService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/admin/operacionesbd")
public class OperacionBDController extends BaseController<OperacionBD, String>{

    @Autowired
    private OperacionBDService operacionBDService;

    public OperacionBDController(OperacionBDService operacionBDService) {
        super(operacionBDService);
    }

    @GetMapping
    public ResponseEntity<List<OperacionBD>> getAllOperacionesBD() {
        List<OperacionBD> operaciones = operacionBDService.findAll();
        return ResponseEntity.ok(operaciones);
    }
    
    @PostMapping
    public ResponseEntity<OperacionBD> createOperacionBD(@RequestBody OperacionBD operacionBD) {
        OperacionBD nuevaOperacion = operacionBDService.save(operacionBD);
        return ResponseEntity
        .status(HttpStatus.CREATED)
        
        // no se si es necesario
        .body(nuevaOperacion);
    }

    // Me parece que no hace falta ya que nunca voy a buscar operaciones, modificarlas ni eliminarlas,.
    // Es simplemente para ver un historial.

    // @GetMapping("/{id}")
    // public ResponseEntity<OperacionBD> getOperacionBDById(@PathVariable Long id) {
    //     Optional<OperacionBD> operacion = operacionBDService.getOperacionBDById(id);
    //     return operacion.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
    //             .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    // }

    // @PutMapping("/{id}")
    // public ResponseEntity<OperacionBD> updateOperacionBD(@PathVariable Long id, @RequestBody OperacionBD operacionBD) {
    //     return operacionBDService.getOperacionBDById(id).map(existingOperacion -> {
    //         existingOperacion.setNombreOperacion(operacionBD.getNombreOperacion());
    //         existingOperacion.setTipo(operacionBD.getTipo());
    //         existingOperacion.setFechaRealizacion(operacionBD.getCreatedAt());
    //         existingOperacion.setUsuario(operacionBD.getUsuario());
    //         existingOperacion.setEstado(operacionBD.getEstado());
    //         OperacionBD updatedOperacion = operacionBDService.saveOperacionBD(existingOperacion);
    //         return new ResponseEntity<>(updatedOperacion, HttpStatus.OK);
    //     }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    // }

    // @DeleteMapping("/{id}")
    // public ResponseEntity<HttpStatus> deleteOperacionBD(@PathVariable Long id) {
    //     try {
    //         operacionBDService.deleteOperacionBD(id);
    //         return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    //     } catch (Exception e) {
    //         return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    //     }
    // }

}