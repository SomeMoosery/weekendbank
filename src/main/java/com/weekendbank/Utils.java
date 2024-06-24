package com.weekendbank;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class Utils {
    /**
     * Returns a mapper with some default settings baked in
     * @return The object mapper we'll want to use across the application
     */
    public static ObjectMapper weekendObjectMapper() {
        return new ObjectMapper()
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule())
                .registerModule(new GuavaModule())
                .setSerializationInclusion(JsonInclude.Include.NON_ABSENT)
                .configure(
                        SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
                        false) // serialize dates as strings
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
}
