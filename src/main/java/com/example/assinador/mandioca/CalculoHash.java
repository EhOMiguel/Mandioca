package com.example.assinador.mandioca;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CalculoHash {
    public static BigInteger calcular(MultipartFile arquivo) {
        try {
            // Obtem o conteúdo do arquivo
            byte[] arquivoBytes = arquivo.getBytes();

            // Concatena o token com o conteúdo do arquivo (tirei o token)
            byte[] dataToHash = new byte[arquivoBytes.length];
            System.arraycopy(arquivoBytes, 0, dataToHash, 0, arquivoBytes.length);
            // System.arraycopy(token.getBytes(), 0, dataToHash, arquivoBytes.length, token.getBytes().length);

            // Calcula a hash SHA-256 do conteúdo do arquivo
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(dataToHash);

            // Converte a hash em formato hexadecimal para decimal
            BigInteger hashDecimal = new BigInteger(1, hash);

            System.out.println("HASH DO ARTHUR" + hashDecimal);

            // Converte para string para extrair os primeiros 5 dígitos
            String hashString = hashDecimal.toString();
            String firstFiveDigits = hashString.length() > 5 ? hashString.substring(0, 5) : hashString;

            // Converte os primeiros 5 dígitos de volta para BigInteger
            BigInteger firstFiveDigitsBigInteger = new BigInteger(firstFiveDigits);

            System.out.println("HASH DO ARTHUR 5 digitos" + firstFiveDigitsBigInteger);

            // Retorna apenas os primeiros 5 dígitos em formato BigInteger
            return firstFiveDigitsBigInteger;

        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null; // Retorna null em caso de exceção
        }
    }
}