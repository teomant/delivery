package crow.teomant.delivery.user.service;

import crow.teomant.delivery.user.model.User;
import crow.teomant.delivery.user.model.UserRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserValue get(Integer id) {

        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("No user with id " + id));

        if (user.getDeleted()) {
            throw new IllegalArgumentException("No user with id " + user.getId());
        }
        //any validations
        
        return new UserValue(user);
    }

    public List<UserValue> getAll() {
        //any validations

        return userRepository.getAll().stream()
            .filter(user -> !user.getDeleted())
            .map(UserValue::new)
            .collect(Collectors.toList());
    }

    public UserValue create(UserCreate create) {

        if (userRepository.findByUsername(create.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already used");
        }

        if (create.getBirthDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Are you from the future?");
        }

        //any other validations

        return new UserValue(
            userRepository.save(
                new User(
                    null,
                    create.getUsername(),
                    create.getName(),
                    create.getContactInfo(),
                    create.getEmail(),
                    create.getAddress(),
                    create.getBirthDate(),
                    Collections.emptyList(),
                    LocalDateTime.now(),
                    false
                )
            )
        );
    }

    public UserValue update(UserUpdate update) {
        User user = userRepository.findById(update.getId())
            .orElseThrow(() -> new IllegalArgumentException("No user with id " + update.getId()));

        if (user.getDeleted()) {
            throw new IllegalArgumentException("No user with id " + user.getId());
        }
        //any validations

        user.update(
            update.getName(),
            update.getEmail(),
            update.getContactInfo(),
            update.getAddress(),
            update.getBirthDate()
        );

        return new UserValue(userRepository.save(user));
    }

    @Transactional
    public void delete(Integer id) {
        //any validations

        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("No user with id " + id));
        user.markDeleted();
        userRepository.save(user);
    }
}
