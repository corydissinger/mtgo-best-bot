package com.cd.bot.json;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

/**
 * Created by Cory on 5/16/2017.
 */
public class BotObjectMapper extends ObjectMapper {

    /**
     * Annotation introspector to use for serialization process
     * is configured separately for serialization and deserialization purposes
     */
    public BotObjectMapper() {
        final AnnotationIntrospector introspector
                = new JacksonAnnotationIntrospector();
        super.getDeserializationConfig()
                .with(introspector);
        super.getSerializationConfig()
                .with(introspector);

    }
}
