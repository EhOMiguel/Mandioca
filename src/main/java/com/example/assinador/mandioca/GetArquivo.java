package com.example.assinador.mandioca;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.json.JSONObject;
import java.math.BigInteger;


@RestController
@RequestMapping("/api")
public class GetArquivo {

    @PostMapping("/rota")
    public String arquivo(@RequestParam("arquivo") MultipartFile arquivo,
                       @RequestParam("token") String token) {

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
        BigInteger hash = hashing.calcular(arquivo, token);

        Assinador assinador = new Assinador();
        String assinaturaDec = assinador.getAssinatura(hash, d, n);

        return assinaturaDec;
    }
}
