package indie.repositories.moduloBackup;

import indie.models.moduloBackUp.OperacionBD;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperacionBDRepository extends JpaRepository<OperacionBD, String> {
}