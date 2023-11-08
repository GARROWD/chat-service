package ru.garrowd.chatservice.utils.exceptions.messages;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GenericMessageTimestamp {
    private String message;

    private LocalDateTime timeStamp;

    private String details;

    public GenericMessageTimestamp(String message, String details){
        this.message = message;
        this.details = details;
        timeStamp = LocalDateTime.now();
    }

    public GenericMessageTimestamp(String message){
        this.message = message;
        this.details = null;
        timeStamp = LocalDateTime.now();
    }
}
