package crow.teomant.delivery.user.service;

import java.time.LocalDate;
import lombok.Data;
import lombok.NonNull;

@Data
public class UserCreate {
    @NonNull
    private String name;
    @NonNull
    private String username;
    @NonNull
    private String email;
    @NonNull
    private String address;
    @NonNull
    private String contactInfo;
    @NonNull
    private LocalDate birthDate;
}
