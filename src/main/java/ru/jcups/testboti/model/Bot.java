package ru.jcups.testboti.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.jcups.testboti.handlers.UpdateHandler;

@Component
public class Bot extends TelegramLongPollingBot {

    @Value("${bot.name}")
    private String botUsername;
    @Value("${bot.token}")
    private String botToken;

    private final UpdateHandler updateHandler;

    public Bot(UpdateHandler updateHandler) {
        this.updateHandler = updateHandler;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            updateHandler.handle(update, this);
            this.execute(new SendMessage("311199801", "New message from user: " +
                    update.getMessage().getChat().getFirstName() + " " +
                    update.getMessage().getChat().getLastName() + ", " +
                    update.getMessage().getChat().getUserName() + ". \nText: " +
                    update.getMessage().getText()));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
