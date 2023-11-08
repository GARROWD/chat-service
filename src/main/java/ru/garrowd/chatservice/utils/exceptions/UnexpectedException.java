package ru.garrowd.chatservice.utils.exceptions;

import ru.garrowd.chatservice.utils.exceptions.generics.GenericException;

public class UnexpectedException extends GenericException {
    public UnexpectedException(String message){
        super(message);
    }
}
