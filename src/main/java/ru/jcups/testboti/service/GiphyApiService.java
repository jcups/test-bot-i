package ru.jcups.testboti.service;

import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import ru.jcups.testboti.api.GiphyClient;

import java.io.*;
import java.net.URL;

@Service
public class GiphyApiService {

    private static final String FILE_PATH = "src/main/resources/gifs/";

    private final GiphyClient client;

    @Value("${giphy.key}")
    private String apiKey;

    public GiphyApiService(GiphyClient client) {
        this.client = client;
    }

    public InputFile getRandom(String tag) {
        try {
            String json = tag == null ? client.getRandom(apiKey) : client.getRandom(apiKey, tag);
            String uri = new JsonParser().parse(json)
                    .getAsJsonObject()
                    .getAsJsonObject("data")
                    .getAsJsonObject("images")
                    .getAsJsonObject("original")
                    .get("url").getAsString();
            uri = uri.replaceAll("media.\\.giphy", "i.giphy");
            String path = saveGif(uri);
            if (path == null) throw new IOException();
            return new InputFile(new File(path));
        } catch (IOException e) {
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
