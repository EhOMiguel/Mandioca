package com.example.assinador.mandioca;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;

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

    public byte[] anexarAssinatura(MultipartFile file, byte[] signatureBytes) {
        try {
            ByteArrayInputStream inputPdfStream = new ByteArrayInputStream(file.getBytes());
            ByteArrayOutputStream outputPdfStream = new ByteArrayOutputStream();

            PdfReader reader = new PdfReader(inputPdfStream);
            PdfSigner signer = new PdfSigner(reader, outputPdfStream, new StampingProperties().useAppendMode());

            // Preparar o espaço no documento para a assinatura
            signer.setFieldName("signature");
            ExternalBlankSignatureContainer externalContainer = new ExternalBlankSignatureContainer(PdfName.Adobe_PPKLite, PdfName.Adbe_pkcs7_detached);
            signer.signExternalContainer(externalContainer, 8192);

            // Carrega o documento modificado com espaço reservado para a assinatura
            PdfDocument pdfDoc = new PdfDocument(new PdfReader(new ByteArrayInputStream(outputPdfStream.toByteArray())), new PdfWriter(outputPdfStream));

            // Preenchendo o espaço reservado com a assinatura
            PdfDictionary signatureDictionary = new PdfDictionary();
            signatureDictionary.put(PdfName.Contents, new PdfString(signatureBytes).setHexWriting(true));
            signer.signExternalContainer(new IExternalSignatureContainer() {
                @Override
                public byte[] sign(InputStream data) {
                    return signatureBytes;
                }

                @Override
                public void modifySigningDictionary(PdfDictionary signDic) {
                    signDic.putAll(signatureDictionary);
                }
            }, 8192);

            pdfDoc.close();

            return outputPdfStream.toByteArray();
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
            return null;
        }
    }
}
