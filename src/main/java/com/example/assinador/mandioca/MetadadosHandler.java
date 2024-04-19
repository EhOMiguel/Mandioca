package com.example.assinador.mandioca;

// pdfbox é uma biblioteca que permite criar, manipular e extrair dados de documentos PDF
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
// a classe codec é parte da biblioteca Apache Commons Codec, ela fornece métodos para codificar e decodificar dados em formato Base64
import org.apache.commons.codec.binary.Base64;

public class MetadadosHandler {
    public static String adicionar(MultipartFile arquivo, String token, String hash) {
        try (InputStream inputStream = arquivo.getInputStream();
             PDDocument document = PDDocument.load(inputStream)) {
            PDDocumentInformation info = document.getDocumentInformation();
            info.setCustomMetadataValue("Hash", hash);
            info.setCustomMetadataValue("Token", token);

            // Salva os dados do documento em um fluxo de bytes
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            document.save(byteArrayOutputStream);

            // Retorna os dados do documento com os metadados adicionados
            byte[] pdfData = byteArrayOutputStream.toByteArray();
            String encodedPdf = Base64.encodeBase64String(pdfData);

            // Retorna os dados do documento codificados em Base64
            return encodedPdf;
        } catch (IOException e) {
            e.printStackTrace();
            // Trate o erro conforme necessário
            return null;
        }
    }
}
