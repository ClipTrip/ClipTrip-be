package com.cliptripbe.global.response.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class HttpStatusCodeDeserializer extends JsonDeserializer<HttpStatusCode> {

    @Override
    public HttpStatusCode deserialize(JsonParser p, DeserializationContext ctxt)
        throws IOException {

        String text = p.getText();

        if (text == null || text.isEmpty()) {
            return null;
        }

        try {
            return HttpStatus.valueOf(text);
        } catch (IllegalArgumentException e) {
            return HttpStatus.OK;
        }
    }
}
