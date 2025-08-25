package indie.repositories.moduloBackup;

import indie.dtos.moduloBackup.OperacionBDDTO;
import indie.models.moduloBackUp.OperacionBD;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OperacionBDRepository extends JpaRepository<OperacionBD, String> {

    @Query("SELECT new indie.dtos.moduloBackup.OperacionBDDTO( " +
           "o.nombreOperacion, " +
           "o.createdAt, " +
           "o.usuario.username, " +
           "o.tipo, " +
           "o.estado) " +
           "FROM OperacionBD o")
    List<OperacionBDDTO> findAllOperacionesDTO();

}