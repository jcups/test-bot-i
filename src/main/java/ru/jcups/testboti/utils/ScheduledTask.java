package ru.jcups.testboti.utils;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.jcups.testboti.service.OpenExchangeRatesService;

import java.text.SimpleDateFormat;
import java.util.Date;

@EnableScheduling
@Service
public class ScheduledTask {

    private final OpenExchangeRatesService ratesService;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM y HH:mm:ss");

    public ScheduledTask(OpenExchangeRatesService ratesService) {
        this.ratesService = ratesService;
    }

    @Scheduled(fixedRate = 300000)
    public void reportCurrentTime() {
        ratesService.getLatest();
        System.out.println("Send request to OpenExchangeRates: "+sdf.format(new Date()));
    }
}
