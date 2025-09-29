package indie.repositories.moduloUsuario;

import indie.models.moduloUsuario.Usuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {

    Usuario findByEmailUsuario(String emailUsuario);

    boolean existsByEmailUsuario(String emailUsuario);


    List<Usuario> findAllByDeletedAtIsNull();

    long countBySubTipoUsuario_TipoUsuario_IdAndDeletedAtIsNull(String tipoUsuarioId);

    Optional<Usuario> findByEmailUsuarioIgnoreCase(String emailUsuario);

    boolean existsByEmailUsuarioIgnoreCase(String emailUsuario);

    Usuario findByUsername(String username);
    
    /**
     * Busca usuarios cuyo nombre de usuario contiene la cadena dada, ignorando mayúsculas/minúsculas
     * @param username Parte del nombre de usuario a buscar
     * @return Lista de usuarios que coinciden con el criterio de búsqueda
     */
    @org.springframework.data.jpa.repository.Query(value = "SELECT DISTINCT u FROM Usuario u LEFT JOIN FETCH u.subTipoUsuario WHERE (LOWER(u.username) LIKE LOWER(CONCAT('%', :username, '%')) OR LOWER(u.nombreUsuario) LIKE LOWER(CONCAT('%', :username, '%')) OR LOWER(u.apellidoUsuario) LIKE LOWER(CONCAT('%', :username, '%'))) AND u.deletedAt IS NULL")
    List<Usuario> findByUsernameContainingIgnoreCase(String username);
    
    /**
     * Método alternativo de búsqueda usando el método derivado de JPA
     * @param username Parte del nombre de usuario a buscar
     * @return Lista de usuarios que coinciden con el criterio de búsqueda
     */
    List<Usuario> findByUsernameContainingIgnoreCaseAndDeletedAtIsNull(String username);
    
    /**
     * Busca usuarios por cualquier campo de texto que pueda contener el término buscado
     * @param searchTerm Término para buscar en cualquier campo de texto del usuario
     * @return Lista de usuarios que coinciden con el criterio de búsqueda
     */
    @org.springframework.data.jpa.repository.Query(value = "SELECT u FROM Usuario u WHERE " +
            "(LOWER(u.username) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(u.nombreUsuario) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(u.apellidoUsuario) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(u.emailUsuario) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND " +
            "u.deletedAt IS NULL")
    List<Usuario> findByAnyField(String searchTerm);
}
