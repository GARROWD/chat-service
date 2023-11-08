package ru.garrowd.chatservice.models.extra;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomAndReceiver {
    private String id;

    private String fullName;

    private String imgUrl;

    private LocalDateTime lastActivity;

    private Integer unreadMessageCount;

    private LocalDateTime lastMessageDate;
}
