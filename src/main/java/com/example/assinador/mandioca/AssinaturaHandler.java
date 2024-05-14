package com.example.assinador.mandioca;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

public class AssinaturaHandler {

    public byte[] anexar(MultipartFile file, byte[] signature) {
        try {
            // Convertendo MultipartFile para ByteArrayInputStream
            ByteArrayInputStream inputPdfStream = new ByteArrayInputStream(file.getBytes());

            // Preparar ByteArrayOutputStream para o PDF de saída
            ByteArrayOutputStream outputPdfStream = new ByteArrayOutputStream();

            // Carregar o documento PDF
            PdfDocument pdfDoc = new PdfDocument(new PdfReader(inputPdfStream), new PdfWriter(outputPdfStream));

            // Acessar o formulário no PDF
            PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);

            // Converter a assinatura em uma string base64
            String base64Signature = Base64.getEncoder().encodeToString(signature);

            System.out.println("Assinatura em Base64: " + base64Signature);

            // Criar um campo de formulário para a assinatura, se não existir
            if (form.getField("signature") == null) {
                PdfFormField signatureField = PdfFormField.createText(pdfDoc);
                signatureField.setFieldName("signature");
                form.addField(signatureField);
            }

            // Definir o valor do campo de assinatura
            form.getField("signature").setValue(base64Signature);

            // Fechar o documento (isto também salva o documento)
            pdfDoc.close();

            // Retornar o PDF como array de bytes
            return outputPdfStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
