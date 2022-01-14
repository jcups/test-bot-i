package ru.jcups.testboti.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "unsplash", url = "https://api.unsplash.com/")
public interface UnsplashClient {
    @GetMapping("/photos/random?client_id={accessKey}")
    String getRandom(@PathVariable String accessKey);
}
