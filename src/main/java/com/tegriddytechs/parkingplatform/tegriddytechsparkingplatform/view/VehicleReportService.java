package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.view;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.ParkingSpace;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.ParkingTicket;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.Vehicle;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.VehicleStatus;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.VehicleType;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class VehicleReportService {

    private VehicleReportService() {}

    public static void generateVehicleReportPdf(File outFile,
                                                List<Vehicle> vehicles,
                                                List<ParkingTicket> tickets) throws IOException {

        vehicles = (vehicles == null) ? List.of() : vehicles;
        tickets   = (tickets == null)   ? List.of() : tickets;

        long totalVehicles = vehicles.size();
        long parkedVehicles = vehicles.stream()
                .filter(v -> v != null && v.getVehicleStatus() == VehicleStatus.PARKED)
                .count();
        long notParkedVehicles = Math.max(0, totalVehicles - parkedVehicles);

        // Conteo por tipo
        Map<VehicleType, Long> byType = vehicles.stream()
                .filter(Objects::nonNull)
                .filter(v -> v.getVehicleType() != null)
                .collect(Collectors.groupingBy(Vehicle::getVehicleType, Collectors.counting()));

        List<ParkingTicket> closedTickets = tickets.stream()
                .filter(Objects::nonNull)
                .filter(t -> t.getEntryTime() != null)
                .filter(t -> t.getExitTime() != null)
                .toList();

        // Top 5 por frecuencia (placa)
        PlateStats plateStats = computePlateStatsFromTickets(closedTickets);

        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.LETTER);
            doc.addPage(page);

            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {
                float margin = 54;
                float y = page.getMediaBox().getHeight() - margin;

                // Título
                y = writeTitle(cs, margin, y, "Reporte de Vehículos - Sistema de Parqueos");

                // Fecha/Hora
                y = writeSmall(cs, margin, y, "Generado: " + LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

                y -= 14;

                // Sección: Resumen
                y = writeSectionHeader(cs, margin, y, "Resumen");
                y = writeKeyValue(cs, margin, y, "Total vehículos registrados", String.valueOf(totalVehicles));
                y = writeKeyValue(cs, margin, y, "Vehículos actualmente parqueados", String.valueOf(parkedVehicles));
                y = writeKeyValue(cs, margin, y, "Vehículos no parqueados", String.valueOf(notParkedVehicles));

                y -= 10;

                // Sección: Distribución por tipo
                y = writeSectionHeader(cs, margin, y, "Distribución por tipo");
                if (byType.isEmpty()) {
                    y = writeSmall(cs, margin, y, "No hay datos de tipos de vehículo.");
                } else {
                    for (Map.Entry<VehicleType, Long> e : sortByValueDesc(byType).entrySet()) {
                        y = writeKeyValue(cs, margin, y, String.valueOf(e.getKey()), String.valueOf(e.getValue()));
                        if (y < margin + 120) break; // evitar salirse de la página
                    }
                }

                y -= 10;

                // Nota de limitación (sin sonar “error”, solo aclaración)
                y = writeSmall(cs, margin, y,
                        "Nota: La placa se obtiene desde Ticket -> ParkingSpace -> parkedVehicle. "
                                + "Si el espacio se limpia al cerrar el ticket, algunos tickets no aportarán placa.");

            }

            doc.save(outFile);
        }
    }

    private static class PlateStats {
        final List<Map.Entry<String, Long>> topByCount;
        final List<Map.Entry<String, Double>> topByRevenue;

        PlateStats(List<Map.Entry<String, Long>> topByCount,
                   List<Map.Entry<String, Double>> topByRevenue) {
            this.topByCount = topByCount;
            this.topByRevenue = topByRevenue;
        }
    }

    private static PlateStats computePlateStatsFromTickets(List<ParkingTicket> closedTickets) {
        // Extraer placa (si existe)
        Function<ParkingTicket, String> plateFn = t -> {
            ParkingSpace s = t.getParkingSpace();
            if (s == null) return null;
            Vehicle v = s.getParkedVehicle();
            if (v == null) return null;
            String plate = v.getPlate();
            return (plate == null) ? null : plate.trim();
        };

        // Conteo por placa
        Map<String, Long> countByPlate = closedTickets.stream()
                .map(plateFn)
                .filter(p -> p != null && !p.isEmpty())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        // Ingresos por placa
        Map<String, Double> revenueByPlate = closedTickets.stream()
                .filter(t -> t.getAmountPaid() >= 0)
                .map(t -> Map.entry(plateFn.apply(t), t.getAmountPaid()))
                .filter(e -> e.getKey() != null && !e.getKey().isEmpty())
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.summingDouble(Map.Entry::getValue)
                ));

        List<Map.Entry<String, Long>> topCount = countByPlate.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
                .limit(5)
                .toList();

        List<Map.Entry<String, Double>> topRevenue = revenueByPlate.entrySet().stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(5)
                .toList();

        return new PlateStats(topCount, topRevenue);
    }

    private static <K> Map<K, Long> sortByValueDesc(Map<K, Long> map) {
        return map.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (x, y) -> x,
                        LinkedHashMap::new
                ));
    }

    private static float writeTitle(PDPageContentStream cs, float x, float y, String text) throws IOException {
        cs.beginText();
        cs.setFont(PDType1Font.HELVETICA_BOLD, 18);
        cs.newLineAtOffset(x, y);
        cs.showText(text);
        cs.endText();
        return y - 22;
    }

    private static float writeSectionHeader(PDPageContentStream cs, float x, float y, String text) throws IOException {
        cs.beginText();
        cs.setFont(PDType1Font.HELVETICA_BOLD, 13);
        cs.newLineAtOffset(x, y);
        cs.showText(text);
        cs.endText();
        return y - 16;
    }

    private static float writeKeyValue(PDPageContentStream cs, float x, float y, String key, String value) throws IOException {
        cs.beginText();
        cs.setFont(PDType1Font.HELVETICA_BOLD, 12);
        cs.newLineAtOffset(x, y);
        cs.showText(key + ":");
        cs.endText();

        cs.beginText();
        cs.setFont(PDType1Font.HELVETICA, 12);
        cs.newLineAtOffset(x + 260, y);
        cs.showText(value);
        cs.endText();

        return y - 16;
    }

    private static float writeTableHeader(PDPageContentStream cs, float x, float y, String c1, String c2) throws IOException {
        cs.beginText();
        cs.setFont(PDType1Font.HELVETICA_BOLD, 11);
        cs.newLineAtOffset(x, y);
        cs.showText(c1);
        cs.endText();

        cs.beginText();
        cs.setFont(PDType1Font.HELVETICA_BOLD, 11);
        cs.newLineAtOffset(x + 260, y);
        cs.showText(c2);
        cs.endText();

        return y - 14;
    }

    private static float writeTableRow(PDPageContentStream cs, float x, float y, String v1, String v2) throws IOException {
        cs.beginText();
        cs.setFont(PDType1Font.HELVETICA, 11);
        cs.newLineAtOffset(x, y);
        cs.showText(v1);
        cs.endText();

        cs.beginText();
        cs.setFont(PDType1Font.HELVETICA, 11);
        cs.newLineAtOffset(x + 260, y);
        cs.showText(v2);
        cs.endText();

        return y - 14;
    }

    private static float writeSmall(PDPageContentStream cs, float x, float y, String text) throws IOException {
        cs.beginText();
        cs.setFont(PDType1Font.HELVETICA, 9);
        cs.newLineAtOffset(x, y);
        cs.showText(text);
        cs.endText();
        return y - 12;
    }

    private static String formatMoney0(double v) {
        return String.format("₡ %.0f", v);
    }
}