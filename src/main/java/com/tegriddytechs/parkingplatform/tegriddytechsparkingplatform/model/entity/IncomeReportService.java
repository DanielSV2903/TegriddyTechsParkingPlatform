package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity;


import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class IncomeReportService {

    public static void generateIncomeReportPdf(File outFile, List<ParkingTicket> tickets) throws IOException {

        // Filtramos tickets "válidos" para ingresos
        List<ParkingTicket> closed = tickets.stream()
                .filter(t -> t.getEntryTime() != null)
                .filter(t -> t.getExitTime() != null)
                .collect(Collectors.toList());

        LocalDate today = LocalDate.now();
        YearMonth currentMonth = YearMonth.now();

        // Ingresos hoy
        double incomeToday = closed.stream()
                .filter(t -> t.getEntryTime().toLocalDate().equals(today))
                .mapToDouble(ParkingTicket::getAmountPaid)
                .sum();

        long ticketsToday = closed.stream()
                .filter(t -> t.getEntryTime().toLocalDate().equals(today))
                .count();

        // Ingresos mes actual
        double incomeMonth = closed.stream()
                .filter(t -> YearMonth.from(t.getEntryTime()).equals(currentMonth))
                .mapToDouble(ParkingTicket::getAmountPaid)
                .sum();

        long ticketsMonth = closed.stream()
                .filter(t -> YearMonth.from(t.getEntryTime()).equals(currentMonth))
                .count();

        // Ingresos total
        double incomeTotal = closed.stream()
                .mapToDouble(ParkingTicket::getAmountPaid)
                .sum();

        long ticketsTotal = closed.size();

        double avgTicket = ticketsTotal > 0 ? (incomeTotal / ticketsTotal) : 0;

        // Top 5 días por ingreso
        Map<LocalDate, Double> incomeByDay = closed.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getEntryTime().toLocalDate(),
                        Collectors.summingDouble(ParkingTicket::getAmountPaid)
                ));

        List<Map.Entry<LocalDate, Double>> topDays = incomeByDay.entrySet().stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(5)
                .toList();

        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.LETTER);
            doc.addPage(page);

            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {
                float margin = 54;
                float y = page.getMediaBox().getHeight() - margin;

                // Header
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_BOLD, 18);
                cs.newLineAtOffset(margin, y);
                cs.showText("Reporte de Ingresos - Sistema de Parqueos");
                cs.endText();

                y -= 18;

                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA, 10);
                cs.newLineAtOffset(margin, y);
                cs.showText("Generado: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                cs.endText();

                y -= 28;

                // Resumen
                y = writeKeyValue(cs, margin, y, "Ingresos hoy", formatMoney(incomeToday));
                y = writeKeyValue(cs, margin, y, "Tickets cerrados hoy", String.valueOf(ticketsToday));

                y -= 6;

                y = writeKeyValue(cs, margin, y, "Ingresos mes actual", formatMoney(incomeMonth));
                y = writeKeyValue(cs, margin, y, "Tickets cerrados mes actual", String.valueOf(ticketsMonth));

                y -= 6;

                y = writeKeyValue(cs, margin, y, "Ingresos totales", formatMoney(incomeTotal));
                y = writeKeyValue(cs, margin, y, "Tickets cerrados totales", String.valueOf(ticketsTotal));
                y = writeKeyValue(cs, margin, y, "Ticket promedio", formatMoney(avgTicket));

                y -= 18;

                // Top días
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_BOLD, 13);
                cs.newLineAtOffset(margin, y);
                cs.showText("Top 5 días con más ingresos");
                cs.endText();

                y -= 16;

                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_BOLD, 11);
                cs.newLineAtOffset(margin, y);
                cs.showText("Fecha");
                cs.endText();

                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_BOLD, 11);
                cs.newLineAtOffset(margin + 180, y);
                cs.showText("Ingresos");
                cs.endText();

                y -= 14;

                DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                cs.setFont(PDType1Font.HELVETICA, 11);

                for (var e : topDays) {
                    cs.beginText();
                    cs.setFont(PDType1Font.HELVETICA, 11);
                    cs.newLineAtOffset(margin, y);
                    cs.showText(e.getKey().format(df));
                    cs.endText();

                    cs.beginText();
                    cs.setFont(PDType1Font.HELVETICA, 11);
                    cs.newLineAtOffset(margin + 180, y);
                    cs.showText(formatMoney(e.getValue()));
                    cs.endText();

                    y -= 14;
                    if (y < margin + 40) break; // simple corte por si se baja mucho
                }
            }

            doc.save(outFile);
        }
    }

    private static float writeKeyValue(PDPageContentStream cs, float x, float y, String key, String value) throws IOException {
        cs.beginText();
        cs.setFont(PDType1Font.HELVETICA_BOLD, 12);
        cs.newLineAtOffset(x, y);
        cs.showText(key + ":");
        cs.endText();

        cs.beginText();
        cs.setFont(PDType1Font.HELVETICA, 12);
        cs.newLineAtOffset(x + 220, y);
        cs.showText(value);
        cs.endText();

        return y - 16;
    }

    private static String formatMoney(double v) {
        return String.format("CRC %.0f", v);
    }
}