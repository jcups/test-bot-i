package ru.jcups.testboti.service;

import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.jcups.testboti.api.GiphyClient;
import ru.jcups.testboti.model.Bot;
import ru.jcups.testboti.utils.FUtils;
import ru.jcups.testboti.utils.Messages;

import java.io.File;

@Service
public class GiphyApiService {

    private final GiphyClient client;

    @Value("${giphy.key}")
    private String apiKey;

    public GiphyApiService(GiphyClient client) {
        this.client = client;
    }

    public void getRandom(String chatId, String tag, Bot bot) {
        String json = tag == null ? client.getRandom(apiKey) : client.getRandom(apiKey, tag);
        File file = FUtils.saveFile(getURIFromJson(json), ".gif");
        try {
            if (file == null) {
                bot.execute(new SendMessage(chatId, Messages.ERROR_SENDING_FILE));
            } else {
                bot.execute(new SendAnimation(chatId, new InputFile(file)));
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private String getURIFromJson(String json) {
        String uri = new JsonParser().parse(json)
                .getAsJsonObject()
                .getAsJsonObject("data")
                .getAsJsonObject("images")
                .getAsJsonObject("original")
                .get("url").getAsString();
        return uri.replaceAll("media.\\.giphy", "i.giphy");
    }
}
