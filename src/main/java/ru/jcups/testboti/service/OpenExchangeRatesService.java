package ru.jcups.testboti.service;

import org.springframework.stereotype.Service;
import ru.jcups.testboti.api.OpenExchangeRatesPojo;
import ru.jcups.testboti.api.OpenExchangeRatesClient;
import ru.jcups.testboti.utils.Messages;

import java.util.Map;

@Service
public class OpenExchangeRatesService {

    private final OpenExchangeRatesClient client;
    private final String app_id;

    public OpenExchangeRatesService(OpenExchangeRatesClient client, String currenciesKey) {
        this.client = client;
        this.app_id = currenciesKey;
    }

    public String getLatest() {
        OpenExchangeRatesPojo pojo = client.getLatest(app_id);
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, Float> pair : pojo.getRates().entrySet()) {
            builder.append(pair.getKey()).append(" - ").append(pair.getValue()).append("\n");
        }
        return builder.toString();
    }

    public String getLatest(String code) {
        OpenExchangeRatesPojo pojo = client.getLatest(app_id);
        Float value = pojo.getRates().get(code);
        if (value == null) {
            return Messages.CURRENCY_NOT_FOUND;
        } else {
            return value.toString();
        }
    }

    public String getLatest(String from, String to) {
        OpenExchangeRatesPojo pojo = client.getLatest(app_id);
        Float valueFrom = pojo.getRates().get(from);
        Float valueTo = pojo.getRates().get(to);
        float currency = valueFrom/valueTo;
        return Float.toString(currency);
    }
}
