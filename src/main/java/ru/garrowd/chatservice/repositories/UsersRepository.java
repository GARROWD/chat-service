package ru.garrowd.chatservice.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.garrowd.chatservice.models.User;

import java.util.Optional;

@Repository
public interface UsersRepository
        extends JpaRepository<User, String> {
    Optional<User> findByFullName(String fullName);
    Page<User> findAllByFullNameIgnoreCaseStartingWithOrderByFullName(String fullName, Pageable pageable);
}
