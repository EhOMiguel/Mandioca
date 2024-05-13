package com.example.assinador.mandioca;
import java.math.BigInteger;

public class Assinador {
    public static byte[] getAssinatura(BigInteger hash, BigInteger d, BigInteger n) {
        // Calcula a potência de hash elevado a d, que é a assinatura
        BigInteger resultado = hash.modPow(d, n);

        // Converte o resultado para um array de bytes
        byte[] signatureBytes = resultado.toByteArray();

        return signatureBytes;
    }
}
