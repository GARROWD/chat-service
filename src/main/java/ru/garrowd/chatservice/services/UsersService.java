package ru.garrowd.chatservice.services;

import com.university.userservice.grpc.user.UserGrpcServiceGrpc;
import com.university.userservice.grpc.user.UserRequest;
import com.university.userservice.grpc.user.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.garrowd.chatservice.models.User;
import ru.garrowd.chatservice.repositories.UsersRepository;
import ru.garrowd.chatservice.services.validators.ValidationService;
import ru.garrowd.chatservice.utils.enums.ExceptionMessages;
import ru.garrowd.chatservice.utils.exceptions.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UsersService {
    private final UsersRepository usersRepository;

    private final ExceptionMessagesService exceptionMessages;
    private final ValidationService validationService;
    private final ModelMapper modelMapper;

    private final UserGrpcServiceGrpc.UserGrpcServiceBlockingStub stub;

    public User getById(String id)
            throws NotFoundException {
        Optional<User> foundUser = usersRepository.findById(id);

        return foundUser.orElseThrow(() -> new NotFoundException(
                exceptionMessages.getMessage(ExceptionMessages.USER_NOT_FOUND, id)));
    }

    public User getByFullName(String fullName)
            throws NotFoundException {
        Optional<User> foundUser = usersRepository.findByFullName(fullName);

        return foundUser.orElseThrow(() -> new NotFoundException(
                exceptionMessages.getMessage(ExceptionMessages.USER_NOT_FOUND, fullName)));
    }

    public User getByIdOrCreate(String id) {
        return usersRepository.findById(id).orElseGet(() -> {
            // TODO запрос по gRPC для получения пользователя и дальнейшего его создания
            UserResponse response = stub.getUserById(UserRequest.newBuilder().setUserId(id).build());
            return create(User.builder().id(id).fullName(response.getFullName()).imgUrl(response.getImgUrl()).build());
        });
    }

    public void existsById(String id)
            throws NotFoundException {
        usersRepository.findById(id).orElseThrow(() -> new NotFoundException(
                exceptionMessages.getMessage(ExceptionMessages.USER_NOT_FOUND, id)));
    }

    public void updateLastActivityById(String id)
            throws NotFoundException {
        User user = getById(id);

        user.setLastActivity(LocalDateTime.now());

        usersRepository.save(user);
    }

    public List<User> getAllById(List<String> ids) {
        return usersRepository.findAllById(ids);
    }

    public Page<User> getAllByFullNameStartingWith(String fullName, Pageable pageable) {
        return usersRepository.findAllByFullNameIgnoreCaseStartingWithOrderByFullName(fullName, pageable);
    }

    @Transactional
    public User create(User user) {
        // TODO Нужна ли здесь валидация, если я знаю, то мне точно придут правильные данные? Валидация нужная только
        //  на уровне контроллеров?

        usersRepository.save(user);

        log.info("User with ID {} is created", user.getId());

        return user;
    }

    // TODO Как правильно делается редактирование?
    @Transactional
    public User update(User unsavedUser, String id) {
        User user = User.builder().build();
        modelMapper.map(getById(id), user);
        modelMapper.map(unsavedUser, user);
        validationService.validate(user);
        usersRepository.save(user);

        log.info("User with ID {} is updated", user.getId());

        return user;
    }

    @Transactional
    public void updateWithoutChecks(User user) {
        existsById(user.getId());
        usersRepository.save(user);

        log.info("User with ID {} is updated", user.getId());
    }
}
