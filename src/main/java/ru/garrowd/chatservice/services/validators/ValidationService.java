package ru.garrowd.chatservice.services.validators;

import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.garrowd.chatservice.services.ExceptionMessagesService;
import ru.garrowd.chatservice.utils.exceptions.ValidationException;
import ru.garrowd.chatservice.utils.exceptions.messages.GenericMessage;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ValidationService {
    private final ExceptionMessagesService exceptionMessages;
    private final Validator validator;

    public void validate(Object object)
            throws ValidationException {
        Set<GenericMessage> messages =
                validator.validate(object).stream().map(violation -> exceptionMessages.getValidationMessage(
                        violation.getMessage(), violation.getPropertyPath().toString())).collect(Collectors.toSet());

        if(! messages.isEmpty()) {
            throw new ValidationException(messages);
        }
    }

    public boolean isValid(Object object) {
        return validator.validate(object).isEmpty();
    }
}
