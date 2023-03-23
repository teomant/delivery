package crow.teomant.delivery.user.service;

import java.time.LocalDate;
import lombok.Data;
import lombok.NonNull;

@Data
public class UserUpdate {
    @NonNull
    private Integer id;
    @NonNull
    private String name;
    @NonNull
    private String email;
    @NonNull
    private String contactInfo;
    @NonNull
    private String address;
    @NonNull
    private LocalDate birthDate;
}
