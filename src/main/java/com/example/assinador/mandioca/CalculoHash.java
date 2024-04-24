package com.example.assinador.mandioca;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CalculoHash {
    public static BigInteger calcular(MultipartFile arquivo, String token) {
        try {
            // Obtem o conteúdo do arquivo
            byte[] arquivoBytes = arquivo.getBytes();

            // Concatena o token com o conteúdo do arquivo
            byte[] dataToHash = new byte[arquivoBytes.length];
            System.arraycopy(arquivoBytes, 0, dataToHash, 0, arquivoBytes.length);
            // System.arraycopy(token.getBytes(), 0, dataToHash, arquivoBytes.length, token.getBytes().length);

            // Calcula a hash SHA-256 do conteúdo do arquivo
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(dataToHash);

            // Converte a hash em formato hexadecimal para decimal
            BigInteger hashDecimal = new BigInteger(1, hash);

            // Retorna a hash em formato decimal
            return hashDecimal;

        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null; // Retorna null em caso de exceção
        }
    }
}