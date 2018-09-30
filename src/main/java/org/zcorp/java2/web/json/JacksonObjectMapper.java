package org.zcorp.java2.web.json;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JacksonObjectMapper extends ObjectMapper {

    private static final ObjectMapper MAPPER = new JacksonObjectMapper();

    private JacksonObjectMapper() {
        // Это альтернатива @JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, isGetterVisibility = NONE, setterVisibility = NONE):
        // Сначала отключаем доступ к полям и пропертям
        setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        // Потом включаем доступ только к полям
        setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        // Просим не сериализовать null-поля, хотя это работает не всегда
        setSerializationInclusion(JsonInclude.Include.NON_NULL);

        // Это альтернатива @JsonIgnore:
        // Просим не сериализовать Lazy-поля, если они не заполнены
        // Если же они до Jackson уже были заполнены, то сериализуем их
        registerModule(new Hibernate5Module());

        // Просим корректно обрабатывать тип LocalDate/Time
        // Без этого при десериализации будет ошибка из-за отсутствия у LocalDate/Time конструктора по умолчанию
        registerModule(new JavaTimeModule());
        // Просим и LocalDate/Time, и просто Date/Calendar объекты отображаться не как набор цифр, а в читабельном формате
        configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    public static ObjectMapper getMapper() {
        return MAPPER;
    }

}