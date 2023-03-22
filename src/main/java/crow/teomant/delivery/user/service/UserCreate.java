package crow.teomant.delivery.user.service;

import java.time.LocalDate;
import lombok.Data;

@Data
public class UserCreate {
    private String name;
    private String username;
    private String email;
    private String contactInfo;
    private LocalDate birthDate;
}
