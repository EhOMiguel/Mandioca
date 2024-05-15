package com.example.assinador.mandioca;

import org.springframework.web.multipart.MultipartFile;
import com.itextpdf.kernel.pdf.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class CalculoHash {

    public static byte[] calcularHash(MultipartFile arquivo) throws IOException, NoSuchAlgorithmException {
        byte[] arquivoBytes = arquivo.getBytes();
        ByteArrayInputStream inputPdfStream = new ByteArrayInputStream(arquivoBytes);
        PdfReader reader = new PdfReader(inputPdfStream);
        PdfDocument pdfDoc = new PdfDocument(reader);
        byte[] hashResult;

        try {
            PdfDictionary catalog = pdfDoc.getCatalog().getPdfObject();
            PdfDictionary acroForm = catalog.getAsDictionary(PdfName.AcroForm);
            if (acroForm != null) {
                PdfArray fields = acroForm.getAsArray(PdfName.Fields);
                if (fields != null && !fields.isEmpty()) {
                    for (int i = 0; i < fields.size(); i++) {
                        PdfDictionary field = fields.getAsDictionary(i);
                        if (field != null) {
                            PdfDictionary sig = field.getAsDictionary(PdfName.V);
                            if (sig != null) {
                                PdfArray byteRange = sig.getAsArray(PdfName.ByteRange);
                                if (byteRange != null) {
                                    long[] range = byteRange.toLongArray();
                                    hashResult = calcularHashPorByteRange(arquivoBytes, range);
                                    return hashResult;
                                }
                            }
                        }
                    }
                }
            }
            // Calcula a hash do arquivo inteiro se não houver ByteRange
            hashResult = calcularHashInteiro(arquivoBytes);
            return hashResult;
        } finally {
            pdfDoc.close();
        }
    }

    private static byte[] calcularHashPorByteRange(byte[] arquivoBytes, long[] range) throws NoSuchAlgorithmException {
        System.out.println("O BYTERANGE: " + (int) range[1]);
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        // Processa o primeiro intervalo
        digest.update(Arrays.copyOfRange(arquivoBytes, (int) range[0], (int) (range[0] + range[1])));
        // Processa o segundo intervalo
        digest.update(Arrays.copyOfRange(arquivoBytes, (int) range[2], (int) (range[2] + range[3])));
        return digest.digest();
    }

    private static byte[] calcularHashInteiro(byte[] arquivoBytes) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(arquivoBytes);
        return digest.digest();
    }
}
