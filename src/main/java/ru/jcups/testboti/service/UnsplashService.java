package ru.jcups.testboti.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.jcups.testboti.api.unsplash.ImageUnsplashPojo;
import ru.jcups.testboti.api.unsplash.SearchImagesUnsplashPojo;
import ru.jcups.testboti.api.unsplash.UnsplashClient;
import ru.jcups.testboti.model.Bot;
import ru.jcups.testboti.utils.FUtils;
import ru.jcups.testboti.utils.Messages;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UnsplashService {

    private final UnsplashClient client;

    @Value("${unsplash.access_key}")
    private String accessKey;

    public UnsplashService(UnsplashClient client) {
        this.client = client;
    }

    public void getRandom(String chatId, Bot bot) {
        ImageUnsplashPojo pojo = client.getRandom(accessKey);
        File saved = FUtils.saveFile(pojo.getUrls().get("regular"), ".jpg");
        try {
            if (saved == null)
                throw new TelegramApiException();
            SendPhoto sendPhoto = new SendPhoto(chatId, new InputFile(saved));
            if (pojo.getDescription() != null)
                sendPhoto.setCaption(pojo.getDescription());
            bot.execute(sendPhoto);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            try {
                bot.execute(new SendMessage(chatId, Messages.ERROR_SENDING_FILE));
            } catch (TelegramApiException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void searchByTag(String chatId, String tag, Bot bot) {
        SearchImagesUnsplashPojo pojo = client.searchByKeyword(accessKey, tag);
        List<InputMedia> files = new LinkedList<>();
        for (ImageUnsplashPojo image : pojo.getResults().stream().limit(10).collect(Collectors.toList())) {
            File saved = FUtils.saveFile(image.getUrls().get("regular"), ".jpg");
            InputMediaPhoto photo = new InputMediaPhoto();
            assert saved != null;
            photo.setMedia(saved, saved.getName());
            files.add(photo);
        }
        try {
            if (files.isEmpty())
                throw new TelegramApiException();
            bot.execute(new SendMediaGroup(chatId, files));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
