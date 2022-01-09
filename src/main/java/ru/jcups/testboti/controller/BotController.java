package ru.jcups.testboti.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.jcups.testboti.model.Bot;

@RestController
public class BotController {

    private final Bot bot;

    public BotController(Bot bot) {
        this.bot = bot;
    }

    @PostMapping("/")
    public BotApiMethod<?> handle(@RequestBody Update update) {
        bot.onUpdateReceived(update);
        return null;
    }
}
