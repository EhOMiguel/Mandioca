package com.example.assinador.mandioca;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Calendar;

public class AssinaturaHandler {
    public static byte[] anexar(MultipartFile file, byte[] signatureBytes) {
        try (InputStream fileInputStream = file.getInputStream()) {
            PDDocument document = PDDocument.load(fileInputStream);

            // Primeiro salva o documento atual em um ByteArrayOutputStream para capturar todo o conteúdo
            ByteArrayOutputStream tempOutputStream = new ByteArrayOutputStream();
            document.save(tempOutputStream);
            document.close();

            // Reabre o documento do ByteArrayOutputStream para adicionar a assinatura
            InputStream tempInputStream = new ByteArrayInputStream(tempOutputStream.toByteArray());
            document = PDDocument.load(tempInputStream);

            PDSignature signature = new PDSignature();
            signature.setName("Nome do Signatário");
            signature.setLocation("Localização do Signatário");
            signature.setReason("Razão da Assinatura");
            signature.setSignDate(Calendar.getInstance());
            signature.setContents(signatureBytes);

            document.addSignature(signature);

            ByteArrayOutputStream signedContent = new ByteArrayOutputStream();
            // Utiliza saveIncremental para gravar apenas as mudanças (assinatura)
            document.saveIncremental(signedContent);
            document.close();

            System.out.println("Tamanho do conteúdo gravado: " + signedContent.size());

            return signedContent.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
