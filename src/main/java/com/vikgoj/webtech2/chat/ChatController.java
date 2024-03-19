package com.vikgoj.webtech2.chat;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {
   //@messagemappin is an endpoint
    @MessageMapping("chat.sendMessage") //url that i want to use to invoke sendMessage
    @SendTo("/topic/public") //to which topic/which q to send
    //topic comes from WebSocketConfig registry.enableSimpleBroker
    //everytime we get a message, it gets sent to topic/public
    public ChatMessage sendMessage(
          @Payload ChatMessage chatMessage
    ) {
        return chatMessage;
    }

    @MessageMapping("/chat.addUser") // incase user joins, it asks to establish
    //connection between user and websocket
    @SendTo("/topic/public")
    public ChatMessage addUser(
            @Payload ChatMessage chatMessage,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        //Add username in web socket session
        headerAccessor.getSessionAttributes().put("username",chatMessage.getSender());
        return chatMessage;
    }
}
