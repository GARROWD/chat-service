package ru.garrowd.chatservice.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name = "users")
public class User {
    @Id
    @Column(name = "id", unique = true, length = 40)
    private String id;

    @Column(name = "full_name", nullable = false, length = 200)
    private String fullName;

    @Column(name = "img_url", length = 1000)
    private String imgUrl;

    @Column(name = "last_activity")
    private LocalDateTime lastActivity;
}
