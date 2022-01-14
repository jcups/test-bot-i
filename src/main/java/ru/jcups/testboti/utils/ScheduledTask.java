package ru.jcups.testboti.utils;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.jcups.testboti.service.OpenExchangeRatesService;

@EnableScheduling
@Service
public class ScheduledTask {

    private final OpenExchangeRatesService ratesService;

    public ScheduledTask(OpenExchangeRatesService ratesService) {
        this.ratesService = ratesService;
    }

    @Scheduled(fixedRate = 300000)
    public void reportCurrentTime() {
        ratesService.getLatest();
    }
}
