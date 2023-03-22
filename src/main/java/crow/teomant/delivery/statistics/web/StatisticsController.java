package crow.teomant.delivery.statistics.web;

import crow.teomant.delivery.statistics.service.StatisticsRequest;
import crow.teomant.delivery.statistics.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class StatisticsController {
    private final StatisticsService service;

    @PostMapping
    public Object getStatistics(@RequestBody StatisticsRequest request) {
        return service.getStatistics(request);
    }
}
