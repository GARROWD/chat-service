package ru.garrowd.chatservice.utils.exceptions;

import ru.garrowd.chatservice.utils.exceptions.generics.GenericExceptionWithDetails;
import ru.garrowd.chatservice.utils.exceptions.messages.GenericMessage;
import java.util.Set;

public class ValidationException
        extends GenericExceptionWithDetails {
    public ValidationException(Set<GenericMessage> messages) {
        super(messages);
    }
}
