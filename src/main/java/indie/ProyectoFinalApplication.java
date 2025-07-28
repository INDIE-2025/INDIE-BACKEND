package indie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import indie.models.moduloUsuario.Usuario;
import indie.repositories.UsuarioRepository;
import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class ProyectoFinalApplication {

	private final UsuarioRepository usuarioRepository;

	public ProyectoFinalApplication(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}	

public static void main(String[] args) {
	SpringApplication.run(ProyectoFinalApplication.class, args);
	System.out.println("Proyecto Final de Indie - Backend iniciado correctamente.");
}

@PostConstruct
public void init() {
	try {
		Class.forName("com.mysql.cj.jdbc.Driver");
		java.sql.Connection conn = java.sql.DriverManager.getConnection(
			"jdbc:mysql://localhost:3306/indie_db?useSSL=false&serverTimezone=UTC", "root", "root1234");
		if (conn != null && !conn.isClosed()) {
			System.out.println("Conexión a la base de datos exitosa.");
				final Usuario usuario = new Usuario("Admin", "admin@example.com", "password", "ADMIN");
	usuarioRepository.save(usuario);
	System.out.println("Usuario admin creado: " + usuarioRepository.findByEmail(usuario.getEmail()));
		}
	} catch (Exception e) {
		System.out.println("Error al conectar a la base de datos: " + e.getMessage());
	}

}

}
