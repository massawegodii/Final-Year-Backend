package com.massawe.serviceImpl;


import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.massawe.constants.MyConstant;
import com.massawe.dao.ProductDao;
import com.massawe.dao.UserDao;
import com.massawe.entity.Product;
import com.massawe.service.ProductRestService;
import com.massawe.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductRestServiceImpl implements ProductRestService {
    @Autowired
    ProductDao productDao;

    @Autowired
    UserDao userDao;


    @Override
    public ResponseEntity<String> updateAssets(Map<String, String> requestMap) {
        try {
            // Check if the request map contains productId
            if (!requestMap.containsKey("productId")) {
                return MyUtils.getResponseEntity("ProductId is required for updating assets.", HttpStatus.BAD_REQUEST);
            }

            Integer productId = Integer.parseInt(requestMap.get("productId"));

            // Retrieve the Product entity from the database
            Optional<Product> optionalProduct = productDao.findById(productId);

            if (optionalProduct.isPresent()) {
                Product product = optionalProduct.get();

                // Update the fields if they exist in the requestMap
                if (requestMap.containsKey("productName")) {
                    product.setProductName(requestMap.get("productName"));
                }
                if (requestMap.containsKey("productDescription")) {
                    product.setProductDescription(requestMap.get("productDescription"));
                }
                if (requestMap.containsKey("productPrice")) {
                    product.setProductPrice(Double.parseDouble(requestMap.get("productPrice")));
                }
                if (requestMap.containsKey("productModel")) {
                    product.setProductModel(requestMap.get("productModel"));
                }
                if (requestMap.containsKey("productSerialNo")) {
                    product.setProductSerialNo(Integer.parseInt(requestMap.get("productSerialNo")));
                }
                if (requestMap.containsKey("productStatus")) {
                    product.setProductStatus(requestMap.get("productStatus"));
                }
                if (requestMap.containsKey("productCategory")) {
                    product.setProductCategory(requestMap.get("productCategory"));
                }
                if (requestMap.containsKey("productDepartment")) {
                    product.setProductDepartment(requestMap.get("productDepartment"));
                }

                // Save the updated Product entity
                productDao.save(product);

                return MyUtils.getResponseEntity("Product with ID " + productId + " updated successfully.", HttpStatus.OK);
            } else {
                return MyUtils.getResponseEntity("Product with ID " + productId + " not found.", HttpStatus.NOT_FOUND);
            }
        } catch (NumberFormatException e) {
            return MyUtils.getResponseEntity("Invalid number format for one of the fields.", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return MyUtils.getResponseEntity(MyConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public byte[] generatePdf() {

        List<Product> products = (List<Product>) productDao.findAll();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(out);
        com.itextpdf.kernel.pdf.PdfDocument pdfDoc = new com.itextpdf.kernel.pdf.PdfDocument(writer);
        Document document = new Document(pdfDoc, PageSize.A4.rotate());

        // Add the logo
        try {
            String imagePath = "src/main/resources/static/img/logo.png";
            Image img = new Image(ImageDataFactory.create(imagePath));
            img.setWidth(100);
            img.setHeight(60);
            img.setHorizontalAlignment(HorizontalAlignment.LEFT);
            document.add(img);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Add company details
        Paragraph companyDetails = new Paragraph("Smart Asset Management System\nWorkers Compensation Fund,\nDar es salaam, Tanzania")
                .setTextAlignment(TextAlignment.LEFT);
        document.add(companyDetails);

        // Add invoice header
        Paragraph invoiceHeader = new Paragraph("Asset Report")
                .setTextAlignment(TextAlignment.RIGHT)
                .setFontSize(15);
        document.add(invoiceHeader);

        // Add bill to and invoice details
        Table headerTable = new Table(2);
        headerTable.setWidth(UnitValue.createPercentValue(100));

        Cell billToCell = new Cell();
        billToCell.add(new Paragraph("System Expert\nGodfrey Massawe\n+255 753 193 439"));
        billToCell.setBorder(null);

        Cell invoiceDetailsCell = new Cell();

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String formattedDate = currentDate.format(formatter);

        invoiceDetailsCell.add(new Paragraph("Report # 00\nDue Upon Receipt\nIssued " + formattedDate));
        invoiceDetailsCell.setTextAlignment(TextAlignment.RIGHT);
        invoiceDetailsCell.setBorder(null);

        headerTable.addCell(billToCell);
        headerTable.addCell(invoiceDetailsCell);
        document.add(headerTable);

        // Add table header
        float[] columnWidths = {1, 3, 5, 2, 3, 2, 3, 3, 3, 3};
        Table table = new Table(UnitValue.createPercentArray(columnWidths));
        table.setWidth(UnitValue.createPercentValue(100));

        Color headerColor = new DeviceRgb(79, 179, 255);
        Color whiteColor = new DeviceRgb(255, 255, 255);

        String[] headers = {"ID", "Name", "Description", "Price", "Model", "Status", "Serial No", "Department", "Category", "Type"};
        for (String header : headers) {
            Cell headerCell = new Cell().add(new Paragraph(header).setFontColor(whiteColor))
                    .setBackgroundColor(headerColor)
                    .setTextAlignment(TextAlignment.CENTER);
            table.addHeaderCell(headerCell);
        }

        // Add product rows
        for (Product product : products) {
            table.addCell(product.getProductId() != null ? product.getProductId().toString() : "N/A");
            table.addCell(product.getProductName() != null ? product.getProductName() : "N/A");
            table.addCell(product.getProductDescription() != null ? product.getProductDescription() : "N/A");
            table.addCell(product.getProductPrice() != null ? product.getProductPrice().toString() : "N/A");
            table.addCell(product.getProductModel() != null ? product.getProductModel() : "N/A");
            table.addCell(product.getProductStatus() != null ? product.getProductStatus() : "N/A");
            table.addCell(product.getProductSerialNo() != null ? product.getProductSerialNo().toString() : "N/A");
            table.addCell(product.getProductDepartment() != null ? product.getProductDepartment() : "N/A");
            table.addCell(product.getProductCategory() != null ? product.getProductCategory() : "N/A");
            table.addCell(product.getProductType() != null ? product.getProductType() : "N/A");
        }

        document.add(table);
        document.close();

        // Save the PDF to the specified location
//        try (FileOutputStream fos = new FileOutputStream("C:\\Users\\massa\\Downloads\\SAMS.pdf")) {
//            fos.write(out.toByteArray());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return out.toByteArray();
    }
}
