package indie.repositories.moduloUsuario;

import indie.models.moduloUsuario.Usuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    // Métodos para búsqueda
    List<Usuario> findByUsernameContainingIgnoreCaseOrNombreUsuarioContainingIgnoreCaseOrApellidoUsuarioContainingIgnoreCase(
            String username, String nombreUsuario, String apellidoUsuario, Pageable pageable);

    
    /**
     * Busca usuarios cuyo nombre de usuario contiene la cadena dada, ignorando mayúsculas/minúsculas
     * @param username Parte del nombre de usuario a buscar
     * @return Lista de usuarios que coinciden con el criterio de búsqueda
     */
    @Query(value = "SELECT DISTINCT u FROM Usuario u LEFT JOIN FETCH u.subTipoUsuario WHERE (LOWER(u.username) LIKE LOWER(CONCAT('%', :username, '%')) OR LOWER(u.nombreUsuario) LIKE LOWER(CONCAT('%', :username, '%')) OR LOWER(u.apellidoUsuario) LIKE LOWER(CONCAT('%', :username, '%'))) AND u.deletedAt IS NULL")
    List<Usuario> findByUsernameContainingIgnoreCase(@Param("username") String username);
    
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
    @Query(value = "SELECT u FROM Usuario u WHERE " +
            "(LOWER(u.username) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(u.nombreUsuario) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(u.apellidoUsuario) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(u.emailUsuario) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND " +
            "u.deletedAt IS NULL")
    List<Usuario> findByAnyField(@Param("searchTerm") String searchTerm);
    
    /**
     * Busca usuarios artistas que no han sido dados de baja, ordenados por fecha de creación descendente y limitados a cierta cantidad.
     * @param tipoUsuarioId ID del tipo de usuario artista
     * @param limit Número máximo de resultados a devolver
     * @return Lista de artistas ordenados por fecha de creación más reciente
     */
    @Query(value = "SELECT u FROM Usuario u JOIN FETCH u.subTipoUsuario stu JOIN FETCH stu.tipoUsuario tu " +
            "WHERE tu.nombreTipoUsuario = 'ARTISTA' AND u.deletedAt IS NULL " +
            "ORDER BY u.createdAt DESC")
    List<Usuario> findLatestArtistas(org.springframework.data.domain.Pageable pageable);
}
