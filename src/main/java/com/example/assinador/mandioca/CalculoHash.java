package com.example.assinador.mandioca;

import org.springframework.web.multipart.MultipartFile;
import com.itextpdf.kernel.pdf.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class CalculoHash {

    public static byte[] calcularHash(byte[] arquivo) throws IOException, NoSuchAlgorithmException {
        ByteArrayInputStream inputPdfStream = new ByteArrayInputStream(arquivo);
        PdfReader reader = new PdfReader(inputPdfStream);
        PdfDocument pdfDoc = new PdfDocument(reader);

        try {
            PdfDictionary catalog = pdfDoc.getCatalog().getPdfObject();
            PdfDictionary acroForm = catalog.getAsDictionary(PdfName.AcroForm);
            if (acroForm != null) {
                PdfArray fields = acroForm.getAsArray(PdfName.Fields);
                for (int i = 0; i < fields.size(); i++) {
                    PdfDictionary field = fields.getAsDictionary(i);
                    if (field != null) {
                        PdfDictionary sig = field.getAsDictionary(PdfName.V);
                        if (sig != null) {
                            PdfArray byteRange = sig.getAsArray(PdfName.ByteRange);
                            if (byteRange != null) {
                                long[] range = byteRange.toLongArray();
                                return calcularHashPorByteRange(arquivo, range);
                            }
                        }
                    }
                }
            }
        } finally {
            pdfDoc.close();
        }
        throw new IllegalArgumentException("ByteRange não encontrado. O arquivo deve ser preparado para assinatura antes de calcular a hash.");
    }

    private static byte[] calcularHashPorByteRange(byte[] arquivoBytes, long[] range) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        // Processa o primeiro intervalo
        digest.update(Arrays.copyOfRange(arquivoBytes, (int) range[0], (int) (range[0] + range[1])));
        // Processa o segundo intervalo
        digest.update(Arrays.copyOfRange(arquivoBytes, (int) range[2], (int) (range[2] + range[3])));
        return digest.digest();
    }
}
