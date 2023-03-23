package crow.teomant.delivery.user.web;

import crow.teomant.delivery.user.service.UserCreate;
import crow.teomant.delivery.user.service.UserService;
import crow.teomant.delivery.user.service.UserUpdate;
import crow.teomant.delivery.user.service.UserValue;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserValue> getAll() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public UserValue get(@PathVariable Integer id) {
        return userService.get(id);
    }

    @PostMapping
    public UserValue create(@RequestBody UserCreate create) {
        return userService.create(create);
    }

    @PutMapping
    public UserValue update(@RequestBody UserUpdate update) {
        return userService.update(update);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        userService.delete(id);
    }
}
