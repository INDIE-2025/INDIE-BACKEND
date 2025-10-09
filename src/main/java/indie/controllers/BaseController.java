package indie.controllers;

import indie.services.BaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public abstract class BaseController<T, ID> {

    protected final BaseService<T, ID> baseService;

    public BaseController(BaseService<T, ID> baseService) {
        this.baseService = baseService;
    }

    @GetMapping
    public ResponseEntity<List<T>> getAll() {
        return ResponseEntity.ok(baseService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<T> getById(@PathVariable(name = "id") ID id) {
        return ResponseEntity.ok(baseService.findById(id));
    }

    @PostMapping
    public ResponseEntity<T> create(@RequestBody T entity) {
        return ResponseEntity.ok(baseService.save(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<T> update(@PathVariable(name = "id") ID id, @RequestBody T entity) {
        return ResponseEntity.ok(baseService.update(id, entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") ID id) {
        baseService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

