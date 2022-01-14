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

import java.io.*;
import java.net.URL;

@Service
public class UnsplashService {

    private static final String FILE_PATH = "src/main/resources/photos/";

    private final UnsplashClient client;

    @Value("${unsplash.access_key}")
    private String accessKey;

    public UnsplashService(UnsplashClient client) {
        this.client = client;
    }

    public void getRandom(String chatId, Bot bot) {
        String json = client.getRandom(accessKey);
        JsonObject jsonObject = new JsonParser().parse(json)
                .getAsJsonObject();
        String uri = jsonObject.getAsJsonObject("urls")
                .get("regular").getAsString();
        String description;
        if (jsonObject.get("description").isJsonNull())
            description = null;
        else
            description = jsonObject.get("description").getAsString();
        File saved = savePhoto(uri);
        try {
            if (saved == null) {
                bot.execute(new SendMessage(chatId, "Не получилось отправить фото, возможно оно оказалось слишком большим"));
            } else {
                System.out.println("Size photo: " + (saved.length() / (1024.0d * 1024.0d)) + " Mb");
                SendPhoto sendPhoto = new SendPhoto(chatId, new InputFile(saved));
                if (description != null)
                    sendPhoto.setCaption(description);
                bot.execute(sendPhoto);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
            try {
                bot.execute(new SendMessage(chatId, "Не получилось отправить фото, возможно оно оказалось слишком большим"));
            } catch (TelegramApiException ex) {
                ex.printStackTrace();
            }
        }
    }

    private File savePhoto(String uri) {
        try {
            URL url = new URL(uri);
            InputStream is = url.openStream();
            File file = File.createTempFile(FILE_PATH, ".jpg");
            OutputStream os = new FileOutputStream(file);

            byte[] b = new byte[2048];
            int length;

            while ((length = is.read(b)) != -1) {
                os.write(b, 0, length);
            }

            is.close();
            os.close();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
