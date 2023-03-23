package crow.teomant.delivery.user.service;

import crow.teomant.delivery.user.model.User;
import lombok.Value;

@Value
public class UserValue {
    Integer id;
    User.State state;

    public UserValue(User user) {
        this.id = user.getId();
        this.state = user.getCurrentState();
    }
}
