package ru.garrowd.chatservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.garrowd.chatservice.models.Room;
import ru.garrowd.chatservice.models.User;
import ru.garrowd.chatservice.models.extra.RoomAndReceiver;
import ru.garrowd.chatservice.repositories.RoomsRepository;
import ru.garrowd.chatservice.services.validators.ValidationService;
import ru.garrowd.chatservice.utils.enums.ExceptionMessages;
import ru.garrowd.chatservice.utils.exceptions.NotFoundException;
import ru.garrowd.chatservice.utils.exceptions.UnexpectedException;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class RoomsService {
    private final RoomsRepository roomsRepository;
    private final UsersService usersService;

    private final ExceptionMessagesService exceptionMessages;
    private final ValidationService validationService;
    private final ModelMapper modelMapper;

    public Room getById(String id)
            throws NotFoundException {
        Optional<Room> foundRoom = roomsRepository.findById(id);

        return foundRoom.orElseThrow(() -> new NotFoundException(
                exceptionMessages.getMessage(ExceptionMessages.ROOM_NOT_FOUND, id)));
    }

    public Room getByUsersIdOrCreate(String senderId, String receiverId) {
        return roomsRepository.findBySenderIdAndReceiverId(senderId, receiverId).orElseGet(() -> {
            User sender = usersService.getByIdOrCreate(senderId);
            User receiver = usersService.getByIdOrCreate(receiverId);
            return create(Room.builder().senderId(sender.getId()).receiverId(receiver.getId()).build());
        });
    }

    public void existsById(String id)
            throws NotFoundException {
        roomsRepository.findById(id).orElseThrow(() -> new NotFoundException(
                exceptionMessages.getMessage(ExceptionMessages.ROOM_NOT_FOUND, id)));
    }

    public void resetUnreadMessageCountBySenderIdAndReceiverId(String senderId, String receiverId) {
        Room room = getByUsersIdOrCreate(senderId, receiverId);
        room.resetUnreadMessageCount();

        updateWithoutChecks(room);
    }

    // TODO Пожалуйста, скажите как правильно это делать и я сделаю. По мне это лютый колхоз, простите меня за это.
    //  Может лучше сделать два отдельных метода и пусть фронт сам разбирается?
    public Page<RoomAndReceiver> getAllRoomAndReceiverBySenderId(String senderId, Pageable pageable) {
        Page<Room> roomsPage = roomsRepository.findAllBySenderIdOrderByLastMessageDate(senderId, pageable);
        List<Room> rooms = roomsPage.getContent();
        List<User> receivers = usersService.getAllById(rooms.stream().map(Room::getReceiverId).toList());

        if(rooms.size() != receivers.size()) {
            throw new UnexpectedException(
                    exceptionMessages.getMessage(ExceptionMessages.ROOMS_AND_RECEIVERS_COUNT_MISMATCH));
        }

        List<RoomAndReceiver> roomAndReceivers =
                IntStream.range(0, rooms.size()).mapToObj(i -> {
                    Room room = rooms.get(i);
                    User receiver = receivers.get(i);

                    return new RoomAndReceiver(
                            receiver.getId(),
                            receiver.getFullName(),
                            receiver.getImgUrl(),
                            receiver.getLastActivity(),
                            room.getUnreadMessageCount(),
                            room.getLastMessageDate());
                }).toList();

        return new PageImpl<>(roomAndReceivers, pageable, roomsPage.getTotalElements());
    }

    @Transactional
    public Room create(Room room) {
        roomsRepository.save(room);

        log.info("Room with ID {} is created", room.getId());

        return room;
    }

    // TODO Как правильно делается редактирование?
    @Transactional
    public Room update(Room unsavedRoom, String id) {
        Room room = Room.builder().build();
        modelMapper.map(getById(id), room);
        modelMapper.map(unsavedRoom, room);
        validationService.validate(room);
        roomsRepository.save(room);

        log.info("Room with ID {} is updated", room.getId());

        return room;
    }

    @Transactional
    public void updateWithoutChecks(Room room) {
        existsById(room.getId());
        roomsRepository.save(room);
    }
}
