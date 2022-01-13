package ru.jcups.testboti.service;

import at.mukprojects.giphy4j.Giphy;
import at.mukprojects.giphy4j.dao.HttpRequestSender;
import at.mukprojects.giphy4j.entity.search.SearchGiphy;
import at.mukprojects.giphy4j.entity.search.SearchRandom;
import at.mukprojects.giphy4j.exception.GiphyException;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.*;
import java.net.URL;

@Service
public class GiphyApiService {

    private static final String FILE_PATH = "src/main/resources/gifs/";

    private final Giphy giphy;

    public GiphyApiService(String giphyKey) {
        this.giphy = new Giphy(giphyKey, new HttpRequestSender());
    }

    public InputFile getRandom(String tag) {
        try {
            SearchRandom random = giphy.searchRandom(tag);
            String id = random.getData().getId();
            SearchGiphy searchGiphy = giphy.searchByID(id);
            String uri = searchGiphy.getData().getImages().getOriginal().getUrl();
            uri = uri.replaceAll("media.\\.giphy", "i.giphy");
            String path = saveGif(uri);
            if (path == null) throw new IOException();
            return new InputFile(new File(path));
        } catch (GiphyException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String saveGif(String uri) {
        try {
            URL url = new URL(uri);
            InputStream is = url.openStream();
            File file = File.createTempFile(FILE_PATH, ".gif");
            OutputStream os = new FileOutputStream(file);

            byte[] b = new byte[2048];
            int length;

            while ((length = is.read(b)) != -1) {
                os.write(b, 0, length);
            }

            is.close();
            os.close();
            return file.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
