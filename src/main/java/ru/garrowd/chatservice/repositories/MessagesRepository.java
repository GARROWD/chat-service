package ru.garrowd.chatservice.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.garrowd.chatservice.models.Message;

@Repository
public interface MessagesRepository extends JpaRepository<Message, String> {
    Page<Message> findAllBySenderIdAndReceiverIdOrderByDate(String senderId, String receiverId, Pageable pageable);
}
