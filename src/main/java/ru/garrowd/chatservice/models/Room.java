package ru.garrowd.chatservice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// TODO Можно ли создавать класс для связывающей таблицы? Мне не понятно, что делает связь manyToMany в классе user, который имеет set из user. Это же бесконечный цикл...

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", unique = true, length = 40)
    private String id;

    @Column(name = "sender_id", nullable = false, length = 40)
    private String senderId;

    @Column(name = "receiver_id", nullable = false, length = 40)
    private String receiverId;

    @Column(name = "unread_message_count", nullable = false)
    private int unreadMessageCount;

    @Column(name = "last_message_date")
    private LocalDateTime lastMessageDate;

    public void incrementUnreadMessageCount(){
        unreadMessageCount++;
    }

    public void resetUnreadMessageCount(){
        unreadMessageCount = 0;
    }
}
