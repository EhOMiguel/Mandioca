package com.example.assinador.mandioca;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;

import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.signatures.ExternalBlankSignatureContainer;
import com.itextpdf.signatures.PdfSignatureAppearance;
import org.springframework.web.multipart.MultipartFile;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.StampingProperties;
import com.itextpdf.signatures.IExternalSignatureContainer;
import com.itextpdf.signatures.PdfSigner;

public class AssinaturaHandler {
    // Reserva espaço para assinatura
    public byte[] prepararAssinatura(MultipartFile file) throws IOException, GeneralSecurityException {
        ByteArrayInputStream inputPdfStream = new ByteArrayInputStream(file.getBytes());
        ByteArrayOutputStream outputPdfStream = new ByteArrayOutputStream();

        PdfReader reader = new PdfReader(inputPdfStream);
        PdfSigner signer = new PdfSigner(reader, outputPdfStream, new StampingProperties().useAppendMode());

        Rectangle rect = new Rectangle(384, 30,  200, 50);

        PdfSignatureAppearance appearance = signer.getSignatureAppearance()
                .setReason("Assinatura Digital")
                .setLocation("ITI")
                .setSignatureCreator("Assinador Mandioca")
                .setPageRect(rect)
                .setPageNumber(1);

        PdfDictionary sigDict = new PdfDictionary();
        sigDict.put(PdfName.Filter, PdfName.Adobe_PPKLite);
        sigDict.put(PdfName.SubFilter, PdfName.Adbe_pkcs7_detached);

        ExternalBlankSignatureContainer blankContainer = new ExternalBlankSignatureContainer(sigDict);
        signer.signExternalContainer(blankContainer, 8192);  // Finaliza corretamente a criação do campo

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
            public void modifySigningDictionary(PdfDictionary signDic) {
                signDic.put(PdfName.Filter, PdfName.Adobe_PPKLite);
                signDic.put(PdfName.SubFilter, PdfName.Adbe_pkcs7_detached);
            }
        };

        signer.signExternalContainer(signatureContainer, 8192);

        reader.close();
        return outputPdfStream.toByteArray();
    }
}
