package indie.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import indie.models.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Aquí puedes definir métodos personalizados si es necesario
    Usuario findByEmail(String email);
    boolean existsByEmail(String email);
    
    
}
