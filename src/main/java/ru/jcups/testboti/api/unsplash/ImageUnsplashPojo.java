package ru.jcups.testboti.api.unsplash;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ImageUnsplashPojo {
    String id;
    int width;
    int height;
    String description;
    Map<String, String> urls;
    Map<String,String> links;
    int likes;
}
