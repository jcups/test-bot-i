package ru.jcups.testboti.handlers;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.jcups.testboti.service.GiphyApiService;

@Component
public class CommandHandler {

    private final GiphyApiService giphyApiService;

    public CommandHandler(GiphyApiService giphyApiService) {
        this.giphyApiService = giphyApiService;
    }

    public PartialBotApiMethod<?> handle(Message message) {
        String messageText = message.getText().split(" ").length > 1 ?
                message.getText().split(" ")[0] :
                message.getText();

        switch (messageText) {
            case "/gif":
                return getGif(message);
            case "/video":
                break;
            case "/photo":
                break;
            case "/currencies":
                break;
            default:
                break;
        }

        return new SendMessage(String.valueOf(message.getChatId()), messageText);
    }

    private PartialBotApiMethod<?> getGif(Message message) {
        InputFile file = message.getText().split(" ")[1] == null ?
                giphyApiService.getRandom("random") :
                giphyApiService.getRandom(message.getText().split(" ")[1]);

        return file == null ?
                new SendMessage(String.valueOf(message.getChatId()), message.getText()) :
                new SendAnimation(String.valueOf(message.getChatId()), file);
    }
}
