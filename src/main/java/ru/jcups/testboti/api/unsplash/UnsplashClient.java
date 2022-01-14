package ru.jcups.testboti.api.unsplash;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "unsplash", url = "https://api.unsplash.com")
public interface UnsplashClient {

    @GetMapping("/photos/random?client_id={accessKey}")
    ImageUnsplashPojo getRandom(@PathVariable String accessKey);

    @GetMapping("/search/photos?client_id={accessKey}&query={tag}")
    SearchImagesUnsplashPojo searchByKeyword(@PathVariable String accessKey, @PathVariable String tag);
}
