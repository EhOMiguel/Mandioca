package com.example.assinador.mandioca;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.client.RestTemplate;

public class GetChavesPublicas {

    private static GetChavesPublicas instance;
    private Map<String, String> chavesPublicas = new HashMap<>();
    private RestTemplate restTemplate = new RestTemplate();
    private String url = "http://localhost:8082/chavesPublicas";

    // Construtor privado para evitar instanciar diretamente
    private GetChavesPublicas() {}

    // Método estático para obter a instância Singleton
    public static synchronized GetChavesPublicas getInstance() {
        if (instance == null) {
            instance = new GetChavesPublicas();
        }
        return instance;
    }

    public String getChave(String token) {
        // Procura a chave no mapa
        String chave = chavesPublicas.get(token);

        if (chave == null) {
            // Se não encontrar, faz a requisição para obter as chaves públicas
            chave = restTemplate.getForObject(url + "?token=" + token, String.class);
            
            // Armazena o resultado no mapa
            chavesPublicas.put(token, chave);
        }

        return chave;
    }

    public Map<String, String> getChavesPublicas() {
        return chavesPublicas;
    }
}
