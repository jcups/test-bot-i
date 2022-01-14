package ru.jcups.testboti.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.jcups.testboti.api.UnsplashClient;
import ru.jcups.testboti.model.Bot;
import ru.jcups.testboti.utils.FUtils;
import ru.jcups.testboti.utils.Messages;

import java.io.File;

@Service
public class UnsplashService {

    private final UnsplashClient client;

    @Value("${unsplash.access_key}")
    private String accessKey;

    public UnsplashService(UnsplashClient client) {
        this.client = client;
    }

    public void getRandom(String chatId, Bot bot) {
        String json = client.getRandom(accessKey);
        String[] data = getDataFromJson(json);
        File saved = FUtils.saveFile(data[0], ".jpg");
        try {
            if (saved == null) {
                bot.execute(new SendMessage(chatId, Messages.ERROR_SENDING_FILE));
            } else {
                SendPhoto sendPhoto = new SendPhoto(chatId, new InputFile(saved));
                if (data[1] != null)
                    sendPhoto.setCaption(data[1]);
                bot.execute(sendPhoto);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
            try {
                bot.execute(new SendMessage(chatId, Messages.ERROR_SENDING_FILE));
            } catch (TelegramApiException ex) {
                ex.printStackTrace();
            }
        }
    }

    private String[] getDataFromJson(String json) {
        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
        String uri = jsonObject.getAsJsonObject("urls").get("regular").getAsString();
        String description;
        if (jsonObject.get("description").isJsonNull())
            description = null;
        else
            description = jsonObject.get("description").getAsString();
        return new String[]{uri, description};
    }
}
