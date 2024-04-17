package com.example.assinador.mandioca;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class GetArquivo {

    @PostMapping("/rota")
    public String arquivo(@RequestParam("arquivo") MultipartFile arquivo,
                       @RequestParam("token") String token) {
        // Calcula hash do arquivo
        String hash = CalculoHash.calcular(arquivo);

        return MetadadosHandler.adicionar(arquivo, token, hash);
    }
}
