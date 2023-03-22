package crow.teomant.delivery.user.model;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(Integer id);

    List<User> getAll();

    Optional<User> findByUsername(String username);

    User save(User user);

    void delete(Integer id);
}
