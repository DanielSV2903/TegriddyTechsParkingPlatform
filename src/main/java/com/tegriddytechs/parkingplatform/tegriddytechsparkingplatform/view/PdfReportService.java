package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.view;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PdfReportService {

    public static void createGeneralStatsPdf(File outFile,
                                             String totalLots,
                                             String availableSpaces,
                                             String activeVehicles,
                                             String incomeToday) {
        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.LETTER);
            doc.addPage(page);

            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {
                float margin = 54;
                float y = page.getMediaBox().getHeight() - margin;

                // Título
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_BOLD, 18);
                cs.newLineAtOffset(margin, y);
                cs.showText("Reporte - Estadísticas Generales (Parqueos)");
                cs.endText();

                y -= 22;

                // Fecha/hora
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA, 10);
                cs.newLineAtOffset(margin, y);
                cs.showText("Generado: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                cs.endText();

                y -= 28;

                // Bloque de datos (tipo “tabla” simple)
                y = writeRow(cs, margin, y, "Total Parqueaderos", totalLots);
                y = writeRow(cs, margin, y, "Espacios Disponibles", availableSpaces);
                y = writeRow(cs, margin, y, "Vehiculos Activos", activeVehicles);
                y = writeRow(cs, margin, y, "Ingresos Hoy", incomeToday);

                y -= 20;

                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_OBLIQUE, 9);
                cs.newLineAtOffset(margin, y);
                cs.showText("Nota: Los valores se calculan con base al estado actual del sistema.");
                cs.endText();
            }

            doc.save(outFile);
        } catch (IOException e) {
            // Puedes cambiar esto por un Alert en JavaFX si quieres
            e.printStackTrace();
        }
    }

    private static float writeRow(PDPageContentStream cs, float x, float y, String key, String value) throws IOException {
        cs.beginText();
        cs.setFont(PDType1Font.HELVETICA_BOLD, 12);
        cs.newLineAtOffset(x, y);
        cs.showText(key + ": ");
        cs.endText();

        cs.beginText();
        cs.setFont(PDType1Font.HELVETICA, 12);
        cs.newLineAtOffset(x + 180, y);
        cs.showText(value);
        cs.endText();

        return y - 18;
    }
}