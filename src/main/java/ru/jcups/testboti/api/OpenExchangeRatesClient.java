package ru.jcups.testboti.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "openexchangerates",url = "https://openexchangerates.org/api/")
public interface OpenExchangeRatesClient {

    @GetMapping("/latest.json?app_id={app_id}")
    CurrencyPojo getLatest(@PathVariable String app_id);

}
