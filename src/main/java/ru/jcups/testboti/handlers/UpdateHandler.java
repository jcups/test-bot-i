package ru.jcups.testboti.handlers;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.jcups.testboti.model.Bot;

@Component
public class UpdateHandler {

    private final CommandHandler commandHandler;
    private final TextMessageHandler textMessageHandler;

    public UpdateHandler(CommandHandler commandHandler, TextMessageHandler textMessageHandler) {
        this.commandHandler = commandHandler;
        this.textMessageHandler = textMessageHandler;
    }

    public PartialBotApiMethod<?> handle(Update update, Bot bot) throws TelegramApiException {
        System.out.println("inner UpdateHandler.handle");
        Message message = update.getMessage();
        String messageText = message.getText();
        if (messageText.startsWith("/")) {
            return commandHandler.handle(message, bot);
        } else {
            textMessageHandler.handle(message, bot);
            return null;
        }
    }
}
