package org.zcorp.java2.util.converters;

import org.springframework.core.convert.converter.Converter;
import org.zcorp.java2.util.DateTimeUtil;

import java.time.LocalDate;

public class LocalDateConverter implements Converter<String, LocalDate> {
    @Override
    public LocalDate convert(String source) {
        return DateTimeUtil.parseLocalDate(source);
    }
}
