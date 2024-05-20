package com.example.assinador.mandioca;
import java.math.BigInteger;
import java.util.Arrays;

public class Assinador {
    public static byte[] assinar(byte[] hash, BigInteger d, BigInteger n) {
        BigInteger hashInt = new BigInteger(1, hash);

        System.out.println("HASH DO ARQUIVO: " + hashInt);

        // Calcula a potência de hash elevado a d, que é a assinatura
        BigInteger resultado = hashInt.modPow(d, n);

        // Converte o resultado para um array de bytes
        byte[] signatureBytes = resultado.toByteArray();

        System.out.println("Assinatura : " + bytesToHex(signatureBytes));

        BigInteger assInt = new BigInteger(1, signatureBytes);

        System.out.println("Assinatura em BigInteger: " + assInt);

        return signatureBytes;
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
