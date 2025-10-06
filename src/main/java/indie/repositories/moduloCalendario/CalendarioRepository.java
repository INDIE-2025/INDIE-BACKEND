package indie.repositories.moduloCalendario;

import indie.models.moduloCalendario.Calendario;
import indie.models.moduloUsuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CalendarioRepository extends JpaRepository<Calendario, String> {
    
    // Método útil para encontrar calendario por usuario ID
    @Query("SELECT u.calendario FROM Usuario u WHERE u.id = :usuarioId")
    Optional<Calendario> findByUsuarioId(@Param("usuarioId") String usuarioId);

    // Método para encontrar calendario por username (MÁS AMIGABLE)
    @Query("SELECT u.calendario FROM Usuario u WHERE u.username = :username")
    Optional<Calendario> findByUsername(@Param("username") String username);

}
