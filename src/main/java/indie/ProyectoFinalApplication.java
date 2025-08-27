package indie;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProyectoFinalApplication {
    static {
        // Cargar variables del archivo .env
        try {
            Dotenv dotenv = Dotenv.configure()
                    .filename(".env")
                    .load();

            dotenv.entries().forEach(entry -> {
                System.setProperty(entry.getKey(), entry.getValue());
            });
        } catch (Exception e) {
            System.out.println("No se pudo cargar .env: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(ProyectoFinalApplication.class, args);
        System.out.println("Proyecto Final de Indie - Backend iniciado correctamente.");
    }
}
