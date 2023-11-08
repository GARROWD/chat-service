package ru.garrowd.chatservice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "messages")
public class Message {
    @Id
    @Column(name = "id", unique = true, length = 40)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "sender_id", nullable = false, length = 40)
    private String senderId;

    @Column(name = "receiver_id", nullable = false, length = 40)
    private String receiverId;

    @Column(name = "content", nullable = false, length = 1000)
    private String content;
}