package com.example.assinador.mandioca;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class GetArquivo {
    @PostMapping("/rota")
    public String file(@RequestParam("arquivo") MultipartFile arquivo,
                            @RequestParam("token") String token) {

        return "arquivo: " + arquivo.getOriginalFilename() + ", token: " + token;
    }
}