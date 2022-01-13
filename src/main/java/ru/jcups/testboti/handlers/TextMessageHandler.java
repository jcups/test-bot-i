package ru.jcups.testboti.handlers;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.jcups.testboti.model.Bot;
import ru.jcups.testboti.utils.CF;
import ru.jcups.testboti.utils.Messages;

@Component
public class TextMessageHandler {

    public void handle(Message message, Bot bot) throws TelegramApiException {
        String msg = message.getText().trim();

        System.out.println(msg);
        switch (msg.length()) {
            case 6:
                if (msg.matches("\\d{6}")) {
                    String code = msg.substring(0, 3) + "-" + msg.substring(3);
                    sendDepartments(message, bot, msg, code);
                }
                break;
            case 7:
                if (msg.matches("\\d{3}\\D\\d{3}")) {
                    String code = msg.substring(0, 3) + "-" + msg.substring(4);
                    sendDepartments(message, bot, msg, code);
                }
                break;
            default:
                bot.execute(new SendMessage(message.getChatId().toString(), Messages.INCORRECT_INPUT));
                break;
        }
    }

    private void sendDepartments(Message message, Bot bot, String msg, String code) throws TelegramApiException {
        System.out.println(code);
        String[] departments = CF.get(code);
        if (departments != null) {
            for (String department : departments)
                bot.execute(new SendMessage(message.getChatId().toString(), department));
        }
    }
}
