package crow.teomant.delivery.statistics.service;

import java.time.LocalDate;
import lombok.Data;

@Data
public class StatisticsRequest {
    private Integer id;
    private LocalDate from;
    private LocalDate to;
}
