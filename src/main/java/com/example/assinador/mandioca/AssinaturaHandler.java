package com.example.assinador.mandioca;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;

import com.itextpdf.signatures.PdfSignatureAppearance;
import org.springframework.web.multipart.MultipartFile;

import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.StampingProperties;
import com.itextpdf.signatures.ExternalBlankSignatureContainer;
import com.itextpdf.signatures.IExternalSignatureContainer;
import com.itextpdf.signatures.PdfSigner;

public class AssinaturaHandler {

    public byte[] prepararAssinatura(MultipartFile file) throws IOException, GeneralSecurityException {
        ByteArrayInputStream inputPdfStream = new ByteArrayInputStream(file.getBytes());
        ByteArrayOutputStream outputPdfStream = new ByteArrayOutputStream();

        PdfReader reader = new PdfReader(inputPdfStream);
        PdfSigner signer = new PdfSigner(reader, outputPdfStream, new StampingProperties().useAppendMode());

        // Criação de um espaço reservado para a assinatura
        IExternalSignatureContainer blankContainer = new IExternalSignatureContainer() {
            @Override
            public byte[] sign(InputStream data) {
                return new byte[0];  // Nenhum dado assinado ainda, apenas reserva o espaço
            }

            @Override
            public void modifySigningDictionary(com.itextpdf.kernel.pdf.PdfDictionary signDic) {
                // Configura o dicionário sem preencher a assinatura ainda
            }
        };

        // Aplica o contêiner com espaço reservado
        signer.signExternalContainer(blankContainer, 8192);

        reader.close();
        return outputPdfStream.toByteArray();
    }

    // Insere a assinatura digital no espaço reservado
    public byte[] anexarAssinatura(byte[] pdfWithSignatureSpace, byte[] signatureBytes) throws IOException, GeneralSecurityException {
        ByteArrayInputStream inputPdfStream = new ByteArrayInputStream(pdfWithSignatureSpace);
        ByteArrayOutputStream outputPdfStream = new ByteArrayOutputStream();

        PdfReader reader = new PdfReader(inputPdfStream);
        PdfSigner signer = new PdfSigner(reader, outputPdfStream, new StampingProperties().useAppendMode());

        IExternalSignatureContainer signatureContainer = new IExternalSignatureContainer() {
            @Override
            public byte[] sign(InputStream data) {
                return signatureBytes;  // Insere os bytes da assinatura real
            }

            @Override
            public void modifySigningDictionary(com.itextpdf.kernel.pdf.PdfDictionary signDic) {
                signDic.put(com.itextpdf.kernel.pdf.PdfName.Filter, com.itextpdf.kernel.pdf.PdfName.Adobe_PPKLite);
                signDic.put(com.itextpdf.kernel.pdf.PdfName.SubFilter, com.itextpdf.kernel.pdf.PdfName.Adbe_pkcs7_detached);
            }
        };

        // Aplica a assinatura real no espaço reservado
        signer.signExternalContainer(signatureContainer, 8192);

        reader.close();
        return outputPdfStream.toByteArray();
    }
}
