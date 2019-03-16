package com.rps.cucumber.glue;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

class APIClient {
    static String doGet(URI uri) {
        RestTemplate client = new RestTemplate();
        return client.getForObject(uri, String.class);
    }

    static String doPost(URI uri, Object inputData) {
        RestTemplate client = new RestTemplate();
        ResponseEntity<String> responseEntity = client.postForEntity(uri, inputData, String.class);
        return responseEntity.getBody();
    }

}
