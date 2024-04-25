package com.example.assinador.mandioca;
import java.math.BigInteger;

public class Assinador {
    public static String getAssinatura(BigInteger hash, BigInteger d, BigInteger n) {
        // Calcula a potência de hash elevado a d
        BigInteger resultado = hash.modPow(d, n);

        System.out.println("assinatura_decimal: " + resultado);
        System.out.println("assinatura_hexa: " + resultado.toString(16));

        return resultado.toString(16);
    }
}