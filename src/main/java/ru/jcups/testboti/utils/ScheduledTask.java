package ru.jcups.testboti.utils;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.jcups.testboti.model.Bot;
import ru.jcups.testboti.service.OpenExchangeRatesService;

import java.text.SimpleDateFormat;
import java.util.Date;

@EnableScheduling
@Service
public class ScheduledTask {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM y HH:mm:ss");

    private final OpenExchangeRatesService ratesService;
    private final Bot bot;

    public ScheduledTask(OpenExchangeRatesService ratesService, Bot bot) {
        this.ratesService = ratesService;
        this.bot = bot;
    }

    @Scheduled(fixedRate = 1200000)
    public void reportCurrentTime() {
        try {
            bot.execute(new SendMessage("311199801", ratesService.getLatest("BYN")));
            System.out.println("Send request to OpenExchangeRates: "+sdf.format(new Date()));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
