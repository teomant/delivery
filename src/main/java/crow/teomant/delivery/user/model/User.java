package crow.teomant.delivery.user.model;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class User {
    private final Integer id;
    private final String username;
    private String name;
    private String contactInfo;
    private String email;
    private String address;
    private LocalDate birthDate;

    public void update(
        String name,
        String email,
        String contactInfo,
        String address,
        LocalDate birthDate
    ) {
        this.name = name;
        this.email = email;
        this.contactInfo = contactInfo;
        this.address = address;
        this.birthDate = birthDate;
    }


}
