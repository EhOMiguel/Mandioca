package com.example.assinador.mandioca;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CalculoHash {
    public static BigInteger calcular(MultipartFile arquivo) {
        try {
            // Obtem o conteúdo do arquivo em array de bytes
            byte[] arquivoBytes = arquivo.getBytes();

            // Calcula a hash SHA-256 do conteúdo do arquivo
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(arquivoBytes);

            // Converte a hash em formato hexadecimal para decimal
            BigInteger hashDecimal = new BigInteger(1, hash);

            System.out.println("HASH AQUI" + hashDecimal);

            // Retorna a hash decimal no formato BigInteger
            return hashDecimal;

        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null; // Retorna null em caso de exceção
        }
    }
}