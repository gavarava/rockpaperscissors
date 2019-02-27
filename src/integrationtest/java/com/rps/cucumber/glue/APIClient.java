package com.rps.cucumber.glue;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

public class APIClient {
    public static String performGet(URI uri) {
        RestTemplate client = new RestTemplate();
        return client.getForObject(uri, String.class);
    }

    public static String doPost(URI uri, Object inputData) {
        RestTemplate client = new RestTemplate();
        ResponseEntity<String> responseEntity = client.postForEntity(uri, inputData, String.class);
        return responseEntity.getBody();
    }

}
