package ru.antonsibgatulin.bankingservice.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.antonsibgatulin.bankingservice.entity.user.User;

import java.net.ContentHandler;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByUsername(String username);

    User getUserByUsernameAndPassword(String login, String password);

    User getUserByUsername(String username);

    
    User getUserById(Long id);

    Boolean existsByUsername(String login);


    List<User> findAll(Specification<User> specification, Pageable pageable);
}
