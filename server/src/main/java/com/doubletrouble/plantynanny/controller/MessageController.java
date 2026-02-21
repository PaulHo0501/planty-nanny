package com.doubletrouble.plantynanny.controller;

import com.doubletrouble.plantynanny.dto.Command;
import com.doubletrouble.plantynanny.dto.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {
    @MessageMapping("/command")
    @SendTo("/topic/commands")
    public Message greeting(Command command) throws Exception {
        return new Message(command.getType().name());
    }
}
