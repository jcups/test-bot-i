package ru.jcups.testboti.api;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CurrencyPojo {
    String disclaimer;
    String license;
    Long timestamp;
    String base;
    Map<String, Float> rates;
}