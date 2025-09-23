package indie;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvEntry;

import java.util.Map;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProyectoFinalApplication {
    static {
        // Cargar variables del archivo .env y mapear a nombres de propiedades de Spring
        try {
            Dotenv dotenv = Dotenv.configure()
                    .filename(".env")
                    .ignoreIfMissing()
                    .load();

            for (DotenvEntry entry : dotenv.entries()) {
                String key = entry.getKey();
                String value = entry.getValue();

                // 1) Exponer tal cual para placeholders tipo ${SPRING_...}
                System.setProperty(key, value);

                // 2) Mapear variables comunes a nombres de propiedades de Spring
                //    - SERVER_PORT -> server.port
                //    - SPRING_*    -> spring.* (lowercase + '_' -> '.')
                //    - APP_*       -> app.* (lowercase + '_' -> '.')
                if ("SERVER_PORT".equals(key)) {
                    System.setProperty("server.port", value);
                }

                if (key.startsWith("SPRING_")) {
                    String springProp = key.toLowerCase().replace('_', '.');
                    System.setProperty(springProp, value);
                }

                if (key.startsWith("APP_")) {
                    String appProp = "app." + key.substring(4).toLowerCase().replace('_', '.');
                    System.setProperty(appProp, value);
                }
            }
        } catch (Exception e) {
            System.out.println("No se pudo cargar .env: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(ProyectoFinalApplication.class, args);
        System.out.println("Proyecto Final de Indie - Backend iniciado correctamente.");
    }
}
