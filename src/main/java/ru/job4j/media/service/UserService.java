package ru.job4j.media.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.media.entity.User;
import ru.job4j.media.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User save(User user) {
        return userRepository.save(user);
    }

    public boolean update(User user) {
        return userRepository.update(user) > 0L;
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public boolean deleteById(Long id) {
        return userRepository.delete(id) > 0L;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
}
