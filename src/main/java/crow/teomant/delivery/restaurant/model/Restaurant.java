package crow.teomant.delivery.restaurant.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
public class Restaurant {
    private final Integer id;
    private String name;
    private String contactInfo;
    private String info;
    private String address;
    private List<OpeningHours> openingHours;

    public void update(
        String name,
        String info,
        String contactInfo,
        String address,
        List<OpeningHours> openingHours
    ) {
        this.name = name;
        this.info = info;
        this.contactInfo = contactInfo;
        this.address = address;
        this.openingHours = openingHours;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OpeningHours {
        DayOfWeek dayOfWeek;
        //don't do so in production. Use separate dto for web
        @Schema(type = "string", pattern = "HH:MM", example = "10:00")
        LocalTime from;
        @Schema(type = "string", pattern = "HH:MM", example = "10:00")
        LocalTime to;
    }

}
