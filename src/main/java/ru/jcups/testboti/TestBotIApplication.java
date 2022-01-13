package ru.jcups.testboti;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.jcups.testboti.model.Bot;

@SpringBootApplication
@EnableFeignClients
public class TestBotIApplication {

    @Value("${giphy.key}")
    private String giphyKey;

    @Value("${openexchangerates.key}")
    private String currenciesKey;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(TestBotIApplication.class, args);
        Bot bot = context.getBean(Bot.class);
        try {
            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            System.out.println(bot.getBotUsername());
            System.out.println(bot.getBotToken());
            api.registerBot(bot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Bean
    public String giphyKey() {
        return giphyKey;
    }

    @Bean
    public String currenciesKey() {
        return currenciesKey;
    }
}
