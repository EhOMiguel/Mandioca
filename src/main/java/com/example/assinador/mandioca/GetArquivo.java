package com.example.assinador.mandioca;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class GetArquivo {

    @PostMapping("/rota")
    public ResponseEntity<?> arquivo(@RequestParam("arquivo") MultipartFile arquivo,
                       @RequestParam("token") String token) throws Exception {

        if(!arquivo.getOriginalFilename().endsWith(".pdf")){
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Formato de arquivo inválido. Apenas arquivos PDF são aceitos.");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        GetChavesPublicas chavesPublicas = GetChavesPublicas.getInstance();
        String chaves = chavesPublicas.getChave(token);
    
        JSONObject jsonObject = new JSONObject(chaves);

        // Obter os valores de e e n como BigInteger
        BigInteger d = jsonObject.getBigInteger("d");
        BigInteger n = jsonObject.getBigInteger("n");

        // Imprimir os valores
        System.out.println("d: " + d);
        System.out.println("n: " + n);

        AssinaturaHandler handler = new AssinaturaHandler();
        byte[] arquivo_ass = handler.anexarAssinatura(arquivo, d, n);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(arquivo_ass);
    }
}
