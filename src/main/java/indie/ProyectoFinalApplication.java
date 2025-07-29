package indie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import indie.models.moduloUsuario.Usuario;
import indie.repositories.UsuarioRepository;
import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class ProyectoFinalApplication {

public static void main(String[] args) {
	SpringApplication.run(ProyectoFinalApplication.class, args);
	System.out.println("Proyecto Final de Indie - Backend iniciado correctamente.");
}

}
