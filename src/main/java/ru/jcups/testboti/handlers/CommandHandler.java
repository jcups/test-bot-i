package ru.jcups.testboti.handlers;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.jcups.testboti.model.Bot;
import ru.jcups.testboti.service.GiphyApiService;
import ru.jcups.testboti.service.OpenExchangeRatesService;
import ru.jcups.testboti.utils.Messages;

@Component
public class CommandHandler {

    private final GiphyApiService giphyApiService;
    private final OpenExchangeRatesService openExchangeRatesService;


    public CommandHandler(GiphyApiService giphyApiService, OpenExchangeRatesService openExchangeRatesService) {
        this.giphyApiService = giphyApiService;
        this.openExchangeRatesService = openExchangeRatesService;
    }

    public void handle(Message message, Bot bot) {
        String messageText = message.getText().split(" ").length > 1 ?
                message.getText().split(" ")[0] :
                message.getText();
        String chatId = String.valueOf(message.getChatId());
        try {
            switch (messageText) {
                case "/start":
                case "/help":
                    bot.execute(new SendMessage(chatId, Messages.HELP));
                    break;
                case "/gif":
                    SendAnimation send = getGif(message);
                    if (send == null) {
                        bot.execute(new SendMessage(chatId, messageText));
                    } else {
                        bot.execute(send);
                    }
                    break;
                case "/photo":
                    bot.execute(new SendMessage(chatId, "Данная функция находится в разработке"));
                    break;
                case "/currencies":
                    bot.execute(getCurrencies(message));
                    break;
                default:
                    bot.execute(new SendMessage(chatId, messageText));
                    break;
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private SendMessage getCurrencies(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        String [] parts = message.getText().split(" ");
        String response = null;
        switch (parts.length) {
            case 1:
                response = openExchangeRatesService.getLatest();
                break;
            case 2:
                response = openExchangeRatesService.getLatest(parts[1]);
                break;
            case 3:
                response = openExchangeRatesService.getLatest(parts[1], parts[2]);
                break;
        }
        if (response == null || response.isEmpty()) {
            sendMessage.setText(Messages.CURRENCY_NOT_FOUND);
        } else {
            sendMessage.setText(response);
        }
        return sendMessage;
    }

    private SendAnimation getGif(Message message) {
        InputFile file = message.getText().split(" ").length <= 1 ?
                giphyApiService.getRandom(null) :
                giphyApiService.getRandom(message.getText().split(" ")[1]);

        return file == null ?
                null : new SendAnimation(String.valueOf(message.getChatId()), file);
    }
}
