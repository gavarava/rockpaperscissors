package com.rps.cucumber.glue;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

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
