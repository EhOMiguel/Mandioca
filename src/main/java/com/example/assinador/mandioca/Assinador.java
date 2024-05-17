package com.example.assinador.mandioca;
import java.math.BigInteger;

public class Assinador {
    public static byte[] assinar(byte[] hash, BigInteger d, BigInteger n) {
        BigInteger hashInt = new BigInteger(1, hash);

        System.out.println("HASH DO ARQUIVO: " + hashInt);

        // Calcula a potência de hash elevado a d, que é a assinatura
        BigInteger resultado = hashInt.modPow(d, n);

        // Converte o resultado para um array de bytes
        byte[] signatureBytes = resultado.toByteArray();

        return signatureBytes;
    }
}
