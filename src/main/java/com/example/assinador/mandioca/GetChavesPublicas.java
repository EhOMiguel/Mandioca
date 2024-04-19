package com.example.assinador.mandioca;

import org.springframework.web.client.RestTemplate;

public class GetChavesPublicas {
    public String getChave(String token) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:(porta)/chavesPublicas";

        return restTemplate.getForObject(url + "?token=" + token, String.class);
    }
}