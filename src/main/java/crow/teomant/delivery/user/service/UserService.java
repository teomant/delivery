package crow.teomant.delivery.user.service;

import crow.teomant.delivery.user.model.User;
import crow.teomant.delivery.user.model.UserRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User get(Integer id) {
        //any validations

        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No user with id " + id));
    }

    public List<User> getAll() {
        //any validations

        return userRepository.getAll();
    }

    public User create(UserCreate create) {

        if (userRepository.findByUsername(create.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already used");
        }

        if (create.getBirthDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Are you from the future?");
        }

        //any other validations

        return userRepository.save(
            new User(null, create.getUsername(), create.getName(), create.getContactInfo(), create.getEmail(),
                create.getBirthDate())
        );
    }

    public User update(UserUpdate update) {
        User user = userRepository.findById(update.getId())
            .orElseThrow(() -> new IllegalArgumentException("No user with id " + update.getId()));

        //any validations

        user.update(
            update.getName(),
            update.getEmail(),
            update.getContactInfo(),
            update.getBirthDate()
        );

        return userRepository.save(user);
    }

    @Transactional
    public void delete(Integer id) {
        //any validations

        userRepository.delete(id);
    }
}
