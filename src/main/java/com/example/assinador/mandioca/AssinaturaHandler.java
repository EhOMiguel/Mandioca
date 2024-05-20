package com.example.assinador.mandioca;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.GeneralSecurityException;

import com.itextpdf.kernel.pdf.*;
import org.springframework.web.multipart.MultipartFile;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.signatures.IExternalSignatureContainer;
import com.itextpdf.signatures.PdfSignatureAppearance;
import com.itextpdf.signatures.PdfSigner;

public class AssinaturaHandler {
    public byte[] anexarAssinatura(MultipartFile arquivo, BigInteger d, BigInteger n, String token) throws IOException, GeneralSecurityException {
        ByteArrayInputStream inputPdfStream = new ByteArrayInputStream(arquivo.getBytes());
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
        // signer.setFieldName("Signature");
        
        CalculoHash hashing = new CalculoHash();
        Assinador assinador = new Assinador();

        IExternalSignatureContainer signatureContainer = new IExternalSignatureContainer() {
            @Override
            public byte[] sign(InputStream data) throws GeneralSecurityException {
                try {
                    // Aqui você receberá os dados de entrada, que é o conteúdo do PDF para hash
                    byte[] hash = hashing.gerar(data);
                    byte[] signature = assinador.assinar(hash, d, n);
                    return signature;
                } catch (IOException e) {
                    throw new GeneralSecurityException("Erro ao processar a assinatura", e);
                }
            }

            @Override
            public void modifySigningDictionary(PdfDictionary signDic) {
                // Adiciona o token do usuário ao dicionário de assinatura
                signDic.put(new PdfName("UserToken"), new PdfString(token));
            }
        };

        signer.signExternalContainer(signatureContainer, 8192);

        reader.close();
        return outputPdfStream.toByteArray();
    }
}
