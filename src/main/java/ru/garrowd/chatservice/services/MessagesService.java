package ru.garrowd.chatservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.garrowd.chatservice.models.Message;
import ru.garrowd.chatservice.models.Room;
import ru.garrowd.chatservice.repositories.MessagesRepository;
import ru.garrowd.chatservice.utils.exceptions.NotFoundException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MessagesService {
    private final MessagesRepository messagesRepository;
    private final RoomsService roomsService;
    private final UsersService usersService;

    public Page<Message> getAllBySenderIdAndReceiverId(String senderId, String receiverId, Pageable pageable) {
        roomsService.resetUnreadMessageCountBySenderIdAndReceiverId(senderId, receiverId);

        return messagesRepository.findAllBySenderIdAndReceiverIdOrderByDate(senderId, receiverId, pageable);
    }

    @Transactional
    public Message send(Message message)
            throws NotFoundException {
        Room senderRoom = roomsService.getByUsersIdOrCreate(message.getSenderId(), message.getReceiverId());
        Room receiverRoom = roomsService.getByUsersIdOrCreate(message.getReceiverId(), message.getSenderId());

        senderRoom.setLastMessageDate(LocalDateTime.now());
        receiverRoom.setLastMessageDate(LocalDateTime.now());
        receiverRoom.incrementUnreadMessageCount();

        roomsService.updateWithoutChecks(senderRoom);
        roomsService.updateWithoutChecks(receiverRoom);

        usersService.updateLastActivityById(message.getSenderId());

        message.setDate(LocalDateTime.now());

        messagesRepository.save(message);

        // TODO Как логировать такое действие?log.info(message.toString());

        return message;
    }
}
