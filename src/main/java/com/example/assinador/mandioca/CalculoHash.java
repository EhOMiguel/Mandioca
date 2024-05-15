package com.example.assinador.mandioca;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.web.multipart.MultipartFile;

public class CalculoHash {
    public static byte[] calcular(MultipartFile arquivo) {
        try {
            // Obtem o conteúdo do arquivo em array de bytes
            byte[] arquivoBytes = arquivo.getBytes();

            // Calcula a hash SHA-256 do conteúdo do arquivo
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(arquivoBytes);

            System.out.println("HASH AQUI" + hash);

            // Retorna a hash decimal no formato BigInteger
            return hash;

        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null; // Retorna null em caso de exceção
        }
    }
}