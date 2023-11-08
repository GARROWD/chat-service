package ru.garrowd.chatservice.utils.exceptions;

import ru.garrowd.chatservice.utils.exceptions.generics.GenericException;

public class NotFoundException
        extends GenericException {
    public NotFoundException(String message){
        super(message);
    }
}