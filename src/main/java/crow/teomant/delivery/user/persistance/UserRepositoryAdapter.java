package crow.teomant.delivery.user.persistance;

import crow.teomant.delivery.user.model.User;
import crow.teomant.delivery.user.model.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {
    private final UserJpaRepository jpaRepository;

    @Override
    public Optional<User> findById(Integer id) {
        return jpaRepository.findById(id).map(UserEntity::toModel);
    }

    @Override
    public List<User> getAll() {
        return jpaRepository.findAll().stream().map(UserEntity::toModel).collect(Collectors.toList());
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return jpaRepository.findByUsername(username).map(UserEntity::toModel);
    }

    @Override
    public User save(User user) {
        UserEntity userEntity = Optional.ofNullable(user.getId()).map(jpaRepository::getReferenceById)
            .orElse(new UserEntity(null, user.getUsername(), user.getEmail(), user.getContactInfo(), user.getName(),
                user.getBirthDate()));

        userEntity.setName(user.getName());
        userEntity.setEmail(user.getEmail());
        userEntity.setContactInfo(user.getContactInfo());
        userEntity.setBirthDate(user.getBirthDate());

        return jpaRepository.save(userEntity).toModel();
    }

    @Override
    public void delete(Integer id) {
        jpaRepository.deleteById(id);
    }
}
