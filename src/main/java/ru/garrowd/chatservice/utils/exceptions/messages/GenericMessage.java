package ru.garrowd.chatservice.utils.exceptions.messages;

import lombok.Data;

@Data
public class GenericMessage {
    private String message;

    private String details;

    public GenericMessage(String message, String details) {
        this.message = message;
        this.details = details;
    }
}
