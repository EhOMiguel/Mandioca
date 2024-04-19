package com.example.assinador.mandioca;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.math.BigInteger;

@RestController
@RequestMapping("/api")
public class GetArquivo {

    @PostMapping("/rota")
    public String arquivo(@RequestParam("arquivo") MultipartFile arquivo,
                       @RequestParam("token") String token) {

        //getChavesPublicas chavesPublicas = new GetChavesPublicas();
        //String chaves = chavesPublicas.getChave(token);

        return CalculoHash.calcular(arquivo, token);
    }
}
