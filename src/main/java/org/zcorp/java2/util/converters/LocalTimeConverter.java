package org.zcorp.java2.util.converters;

import org.springframework.core.convert.converter.Converter;
import org.zcorp.java2.util.DateTimeUtil;

import java.time.LocalTime;

public class LocalTimeConverter implements Converter<String, LocalTime> {
    @Override
    public LocalTime convert(String source) {
        return DateTimeUtil.parseLocalTime(source);
    }
}
