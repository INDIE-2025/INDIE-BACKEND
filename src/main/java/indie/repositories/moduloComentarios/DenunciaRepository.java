package indie.repositories.moduloComentarios;


import indie.models.moduloComentarios.Denuncia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DenunciaRepository extends JpaRepository<Denuncia, String> {
}
