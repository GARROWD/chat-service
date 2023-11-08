package ru.garrowd.chatservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDateTime;

public class MessagePayload {
    @Schema(name = "MessagePayloadCreate")
    @Data
    public static class Create {
        @Size(max = 40, message = "id.error.maxSize")
        @NotNull(message = "message.error.nullField")
        private String receiverId;

        @Size(max = 1000, message = "message.error.contentMaxSize")
        @NotNull(message = "message.error.nullField")
        private String content;
    }

    @Data
    public static class Update {

    }

    @Schema(name = "MessagePayloadRequest")
    @Data
    public static class Request {
        private String id;

        private LocalDateTime date;

        private String senderId;

        private String receiverId;

        private String content;
    }
}

