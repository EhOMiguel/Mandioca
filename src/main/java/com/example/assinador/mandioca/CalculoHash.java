package com.example.assinador.mandioca;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CalculoHash {
    public static byte[] gerar(InputStream inputStream) throws NoSuchAlgorithmException, IOException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] byteArray = new byte[1024];
        int bytesCount = 0;

        //Lê os dados do InputStream e atualiza o digest.
        while ((bytesCount = inputStream.read(byteArray)) != -1) {
            digest.update(byteArray, 0, bytesCount);
        }

        //Calcula a hash.
        byte[] hash = digest.digest();

        return hash;
    }
}
