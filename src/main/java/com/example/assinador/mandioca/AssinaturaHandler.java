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

    public byte[] anexarAssinatura(MultipartFile file, byte[] signatureBytes) {
        try {
            ByteArrayInputStream inputPdfStream = new ByteArrayInputStream(file.getBytes());
            ByteArrayOutputStream outputPdfStream = new ByteArrayOutputStream();

            PdfReader reader = new PdfReader(inputPdfStream);
            PdfSigner signer = new PdfSigner(reader, outputPdfStream, new StampingProperties().useAppendMode());

            // Definindo a aparência da assinatura
            PdfSignatureAppearance appearance = signer.getSignatureAppearance()
                    .setReason("Assinatura Digital")
                    .setLocation("Sistema de Assinatura")
                    .setSignatureCreator("Meu Sistema");

            // Preparando um contêiner de assinatura em branco para inserção de assinatura
            IExternalSignatureContainer blankContainer = new IExternalSignatureContainer() {
                @Override
                public byte[] sign(InputStream data) {
                    return signatureBytes;  // Aqui você inseriria o processo de assinatura real
                }

                @Override
                public void modifySigningDictionary(PdfDictionary signDic) {
                    signDic.put(PdfName.Filter, PdfName.Adobe_PPKLite);
                    signDic.put(PdfName.SubFilter, PdfName.Adbe_pkcs7_detached);
                }
            };

            // Assinando e fechando o documento
            // O PdfSigner cuida de inserir o ByteRange corretamente
            signer.signExternalContainer(blankContainer, 8192);

            return outputPdfStream.toByteArray();
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
            return null;
        }
    }
}
