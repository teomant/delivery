package crow.teomant.delivery.user.service;

import java.time.LocalDate;
import lombok.Data;

@Data
public class UserUpdate {
    private Integer id;
    private String name;
    private String email;
    private String contactInfo;
    private LocalDate birthDate;
}
