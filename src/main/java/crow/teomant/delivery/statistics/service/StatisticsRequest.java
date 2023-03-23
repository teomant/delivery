package crow.teomant.delivery.statistics.service;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class StatisticsRequest {
    private Integer id;
    private LocalDateTime from;
    private LocalDateTime to;
}
