package com.example.assinador.mandioca;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CalculoHash {
    public static String calcular(MultipartFile arquivo, String token) {
        try {
            // Obtem o conteúdo do arquivo
            byte[] arquivoBytes = arquivo.getBytes();

            // Concatena o token com o conteúdo do arquivo
            byte[] dataToHash = new byte[arquivoBytes.length + token.getBytes().length];
            System.arraycopy(arquivoBytes, 0, dataToHash, 0, arquivoBytes.length);
            System.arraycopy(token.getBytes(), 0, dataToHash, arquivoBytes.length, token.getBytes().length);

            // Calcula a hash SHA-256 do conteúdo do arquivo + token
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(dataToHash);

            // Converte a hash em formato hexadecimal
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            // Retorna a hash em formato hexadecimal
            return hexString.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return "Erro de IO ao processar o arquivo";
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "Algoritmo de hash não disponível";
        }
    }
}