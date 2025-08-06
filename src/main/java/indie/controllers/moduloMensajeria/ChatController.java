package indie.controllers.moduloMensajeria;

import indie.controllers.BaseController;
import indie.models.moduloMensajeria.Chat;
import indie.services.moduloMensajeria.ChatService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
public class ChatController extends BaseController<Chat,Long> {

    public ChatController(ChatService chatService){
        super(chatService);
    }
}
