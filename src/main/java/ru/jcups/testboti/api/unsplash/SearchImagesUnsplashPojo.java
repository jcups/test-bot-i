package ru.jcups.testboti.api.unsplash;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchImagesUnsplashPojo {
    List<ImageUnsplashPojo> results;
}
