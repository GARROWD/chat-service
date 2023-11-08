package ru.garrowd.chatservice.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import ru.garrowd.chatservice.dto.MessagePayload;
import ru.garrowd.chatservice.models.Message;
import ru.garrowd.chatservice.models.User;
import ru.garrowd.chatservice.models.extra.RoomAndReceiver;
import ru.garrowd.chatservice.services.MessagesService;
import ru.garrowd.chatservice.services.RoomsService;
import ru.garrowd.chatservice.services.UsersService;
import ru.garrowd.chatservice.services.validators.ValidationService;
import ru.garrowd.chatservice.utils.Token;
import ru.garrowd.chatservice.utils.enums.JwtClaims;
import ru.garrowd.chatservice.utils.exceptions.NotFoundException;

@PreAuthorize("isAuthenticated()")
@RestController
@RequiredArgsConstructor
@Slf4j
public class Controller {
    // TODO Сначала у меня было несколько контроллеров, но, не знаю зачем, я их обьеденил
    private final MessagesService messagesService;
    private final UsersService usersService;
    private final RoomsService roomsService;

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ValidationService validationService;
    private final ModelMapper modelMapper;

    @GetMapping("/rooms-and-receivers")
    @ResponseStatus(HttpStatus.OK)
    public Page<RoomAndReceiver> getAllRoomAndReceiverBySenderId(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            JwtAuthenticationToken token) {
        // TODO Нужно поменять с ObjectId на UUID
        return roomsService.getAllRoomAndReceiverBySenderId(Token.get(token, JwtClaims.SUB), PageRequest.of(page, size));
    }

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public Page<User> getAll(
            @RequestParam(value = "fullName", defaultValue = "") String fullName,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size) {
        return usersService.getAllByFullNameStartingWith(fullName, PageRequest.of(page, size));
    }

    @GetMapping("/messages/{receiverId}")
    @ResponseStatus(HttpStatus.OK)
    public Page<Message> getAllByRoomId(
            @PathVariable(value = "receiverId") String receiverId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            JwtAuthenticationToken token) {
        // TODO Нужно поменять с ObjectId а UUID
        return messagesService.getAllBySenderIdAndReceiverId(Token.get(token, JwtClaims.SUB), receiverId, PageRequest.of(page, size));
    }

    // TODO Этот метод для удобного тестирования
    @GetMapping("/messages/send")
    @ResponseStatus(HttpStatus.OK)
    public MessagePayload.Request sendMessage(@RequestBody MessagePayload.Create messagePayload, JwtAuthenticationToken token) {
        if(validationService.isValid(messagePayload)) {
            // TODO Нужно поменять с ObjectId на UUID
            Message message = modelMapper.map(messagePayload, Message.class);
            message.setSenderId(Token.get(token, JwtClaims.SUB));

            Message messageEnrich = messagesService.send(message);
            simpMessagingTemplate.convertAndSend(String.format("user/%s", message.getReceiverId()),
                                                 messageEnrich.toString());

            return modelMapper.map(messageEnrich, MessagePayload.Request.class);
        }
        else {
            return null; // TODO Отправить сообщение об ошибке (или null и есть сообщение об ошибке?)
        }
    }

    @MessageMapping("/message")
    public MessagePayload.Request message(@Payload MessagePayload.Create messagePayload, JwtAuthenticationToken token)
            throws NotFoundException {
        if(validationService.isValid(messagePayload)) {
            // TODO Нужно поменять с ObjectId на UUID
            Message message = modelMapper.map(messagePayload, Message.class);
            message.setSenderId(Token.get(token, JwtClaims.SUB));

            Message messageEnrich = messagesService.send(message);
            simpMessagingTemplate.convertAndSend(String.format("user/%s", message.getReceiverId()),
                                                 messageEnrich.toString());

            return modelMapper.map(messageEnrich, MessagePayload.Request.class);
        }
        else {
            return null; // TODO Отправить сообщение об ошибке (или null и есть сообщение об ошибке?)
        }
    }
}