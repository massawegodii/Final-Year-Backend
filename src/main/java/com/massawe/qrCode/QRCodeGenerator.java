package com.massawe.qrCode;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.massawe.entity.Product;
import com.massawe.entity.User;
import com.massawe.serviceImpl.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.image.BufferedImage;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/qr")
public class QRCodeGenerator {
    @Autowired
    private ProductService productService;

    @GetMapping(value = "/generate/{productId}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<BufferedImage> generateQRCode(@PathVariable("productId") Integer productId) {
        try {
            Product product = productService.getProductDetailsById(productId);
            String qrData = "Smart Asset Management System\n" +
                    "Owner: " + product.getUser().getUserFirstName() + " " + product.getUser().getUserLastName() + "\n" +
                    "Name: " + product.getProductName() + "\n" +
                    "Department: " + product.getProductDepartment() + "\n" +
                    "Price: " + product.getProductPrice() + "\n" +
                    "Serial Number: " + product.getProductSerialNo();

            QRCodeWriter barcodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = barcodeWriter.encode(qrData, BarcodeFormat.QR_CODE, 200, 200);

            return new ResponseEntity<>(MatrixToImageWriter.toBufferedImage(bitMatrix), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
