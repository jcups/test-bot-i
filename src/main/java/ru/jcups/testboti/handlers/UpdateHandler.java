package ru.jcups.testboti.handlers;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class UpdateHandler {

    private final CommandHandler commandHandler;

    public UpdateHandler(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    public PartialBotApiMethod<?> handle(Update update){
        System.out.println("inner UpdateHandler.handle");
        Message message = update.getMessage();
        String messageText = message.getText();
        if (messageText.startsWith("/")){
            return commandHandler.handle(message);
        }
        System.out.println(message);
        return new SendMessage(String.valueOf(message.getChatId()), message.getText());
    }
}
