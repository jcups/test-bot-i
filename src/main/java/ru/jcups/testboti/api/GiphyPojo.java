package ru.jcups.testboti.api;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GiphyPojo {
    Map<String, Object> data;
    Map<String, Object> meta;
}
