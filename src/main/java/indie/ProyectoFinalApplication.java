package indie;

import indie.models.moduloUsuario.SubTipoUsuario;
import indie.models.moduloUsuario.TipoUsuario;
import indie.models.moduloUsuario.Usuario;
import indie.models.moduloComentarios.ComentarUsuario;
import indie.repositories.moduloComentarios.ComentarioRepository;
import indie.repositories.moduloUsuario.SubTipoUsuarioRepository;
import indie.repositories.moduloUsuario.TipoUsuarioRepository;
import indie.repositories.moduloUsuario.UsuarioRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ProyectoFinalApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(ProyectoFinalApplication.class, args);
		System.out.println("Proyecto Final de Indie - Backend iniciado correctamente.");




	}
}