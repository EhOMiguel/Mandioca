package com.example.assinador.mandioca;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.json.JSONObject;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class GetArquivo {

    @PostMapping("/rota")
    public ResponseEntity<?> arquivo(@RequestParam("arquivo") MultipartFile arquivo,
                       @RequestParam("token") String token) {

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

        CalculoHash hashing = new CalculoHash();
        BigInteger hash = hashing.calcular(arquivo);

        Assinador assinador = new Assinador();
        byte[] assinaturaDec = assinador.getAssinatura(hash, d, n);

        AssinaturaHandler handler = new AssinaturaHandler();
        byte[] arquivo_ass = handler.anexar(arquivo, assinaturaDec);

        System.out.println(arquivo_ass.length);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(arquivo_ass);
    }
}
