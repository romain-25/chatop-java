package com.openclassroom.chatopjava.service;

import com.openclassroom.chatopjava.dto.MessageDtoRequest;
import com.openclassroom.chatopjava.dto.MessageDtoResponse;
import com.openclassroom.chatopjava.model.MessageModel;
import com.openclassroom.chatopjava.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;
    public MessageDtoResponse creatMessage(MessageDtoRequest messageDtoRequest) {
        MessageModel message = new MessageModel();
        message.setMessage(messageDtoRequest.getMessage());
        message.setCreated_at(new Date());
        message.setRental_id(messageDtoRequest.getRental_id());
        message.setUser_id(messageDtoRequest.getUser_id());
        messageRepository.save(message);
        MessageDtoResponse messageDtoResponse = new MessageDtoResponse();
        messageDtoResponse.setMessage("Votre message à bien été envoyé : " + messageDtoRequest.getMessage());
        return messageDtoResponse;
    }
}
