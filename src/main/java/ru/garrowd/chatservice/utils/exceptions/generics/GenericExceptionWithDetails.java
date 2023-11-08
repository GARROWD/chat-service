package ru.garrowd.chatservice.utils.exceptions.generics;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.garrowd.chatservice.utils.exceptions.messages.GenericMessage;
import java.util.Set;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GenericExceptionWithDetails
        extends RuntimeException{
    private Set<GenericMessage> genericMessageWithDetails;
}
