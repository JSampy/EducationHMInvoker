package ru.jsam.education.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.jsam.education.dto.Request;

import java.io.IOException;

public class RequestHandler {

    public Request parseRequest(String request) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.readValue(request, Request.class);
        } catch (IOException e) {
            return null;
        }
    }
}
