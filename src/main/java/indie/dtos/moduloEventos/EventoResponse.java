package indie.dtos.moduloEventos;

import java.time.LocalDateTime;
import java.util.List;

public class EventoResponse {
  public String id;
  public String titulo;
  public String descripcion;
  public LocalDateTime fechaHoraEvento;
  public String ubicacion;
  public String idUsuario;
  public LocalDateTime createdAt;
  public LocalDateTime updatedAt;
  public List<String> colaboradoresIds; // Lista de IDs de colaboradores
}