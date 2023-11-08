package ru.garrowd.chatservice.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.garrowd.chatservice.models.Room;

import java.util.Optional;

@Repository
public interface RoomsRepository
        extends JpaRepository<Room, String> {
    Optional<Room> findBySenderIdAndReceiverId(String senderId, String receiverId);
    Page<Room> findAllBySenderIdOrderByLastMessageDate(String senderId, Pageable pageable);
}
