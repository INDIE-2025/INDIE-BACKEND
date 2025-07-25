package indie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProyectoFinalApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProyectoFinalApplication.class, args);
		System.out.println("Proyecto Final de Indie - Backend iniciado correctamente.");
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			java.sql.Connection conn = java.sql.DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/indie_db?useSSL=false&serverTimezone=UTC", "root", "root1234");
			if (conn != null && !conn.isClosed()) {
				System.out.println("Conexión a la base de datos exitosa.");
				conn.close();
			}
		} catch (Exception e) {
			System.out.println("Error al conectar a la base de datos: " + e.getMessage());
		}
	}

}
