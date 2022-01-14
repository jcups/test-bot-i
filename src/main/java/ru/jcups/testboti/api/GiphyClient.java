package ru.jcups.testboti.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "giphy", url = "api.giphy.com/v1")
public interface GiphyClient {

    @GetMapping("/gifs/random?api_key={api_key}")
    String getRandom(@PathVariable String api_key);

    @GetMapping("/gifs/random?api_key={api_key}&tag={tag}")
    String getRandom(@PathVariable String api_key, @PathVariable String tag);

}
