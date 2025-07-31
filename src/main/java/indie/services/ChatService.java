package indie.services;

import indie.models.moduloMensajeria.Chat;
import indie.repositories.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatService extends BaseServiceImpl<Chat,String> {

    ChatRepository chatRepository;

    @Autowired
    public ChatService(ChatRepository chatRepository){
        super(chatRepository);
        this.chatRepository = chatRepository;
    }
}
