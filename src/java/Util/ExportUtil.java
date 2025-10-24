package Util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

/**
 * Exportador minimalista a Excel (TSV) y PDF embellecido sin librerías externas.
 * Ahora con soporte para tildes y eñes.
 */
public final class ExportUtil {

    private ExportUtil() {
    }

    // ===========================
    // EXPORTACIÓN A EXCEL (TSV)
    // ===========================
    public static void exportToExcel(HttpServletResponse response, String fileName,
            List<String> headers, List<List<String>> rows) throws IOException {

        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        try (var writer = response.getWriter()) {
            writer.println(joinRow(headers));
            for (List<String> row : rows) {
                writer.println(joinRow(row));
            }
        }
    }

    // ===========================
    // EXPORTACIÓN A PDF EMBELLECIDO
    // ===========================
    public static void exportToPdf(HttpServletResponse response, String fileName, String title,
            List<String> headers, List<List<String>> rows) throws IOException {

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        byte[] pdfData = buildBeautifulPdf(title, headers, rows);
        response.setContentLength(pdfData.length);

        try (OutputStream out = response.getOutputStream()) {
            out.write(pdfData);
            out.flush();
        }
    }

    // ===========================
    // CONSTRUCCIÓN DE PDF
    // ===========================
    private static byte[] buildBeautifulPdf(String title, List<String> headers, List<List<String>> rows)
            throws IOException {

        ByteArrayOutputStream pdf = new ByteArrayOutputStream();
        List<Integer> offsets = new ArrayList<>();

        write(pdf, "%PDF-1.4\n");

        offsets.add(pdf.size());
        write(pdf, "1 0 obj\n<< /Type /Catalog /Pages 2 0 R >>\nendobj\n");

        offsets.add(pdf.size());
        write(pdf, "2 0 obj\n<< /Type /Pages /Kids [3 0 R] /Count 1 >>\nendobj\n");

        offsets.add(pdf.size());
        write(pdf, "3 0 obj\n<< /Type /Page /Parent 2 0 R /MediaBox [0 0 612 792] "
                + "/Contents 4 0 R /Resources << /Font << /F1 5 0 R >> >> >>\nendobj\n");

        String content = buildStyledContent(title, headers, rows);
        // Aquí usamos ISO-8859-1 para soportar tildes y eñes
        byte[] contentBytes = content.getBytes(StandardCharsets.ISO_8859_1);

        offsets.add(pdf.size());
        write(pdf, "4 0 obj\n<< /Length " + contentBytes.length + " >>\nstream\n");
        pdf.write(contentBytes);
        write(pdf, "\nendstream\nendobj\n");

        offsets.add(pdf.size());
        write(pdf, "5 0 obj\n<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica >>\nendobj\n");

        int xrefPosition = pdf.size();
        write(pdf, "xref\n0 6\n");
        write(pdf, "0000000000 65535 f \n");
        for (int offset : offsets) {
            Formatter formatter = new Formatter();
            formatter.format("%010d 00000 n \n", offset);
            write(pdf, formatter.toString());
            formatter.close();
        }

        write(pdf, "trailer\n<< /Size 6 /Root 1 0 R >>\nstartxref\n" + xrefPosition + "\n%%EOF\n");

        return pdf.toByteArray();
    }

    // ===========================
    // CONTENIDO PDF ESTILIZADO
    // ===========================
    private static String buildStyledContent(String title, List<String> headers, List<List<String>> rows) {
        StringBuilder sb = new StringBuilder();
        sb.append("BT\n");

        // Título centrado
        sb.append("/F1 18 Tf\n");
        sb.append("200 760 Td\n");
        sb.append('(').append(escapePdfText(title)).append(") Tj\n");

        // Línea separadora
        sb.append("/F1 12 Tf\n");
        sb.append("0 -20 Td\n");
        sb.append("(=============================================) Tj\n");

        // Encabezados
        sb.append("/F1 11 Tf\n");
        sb.append("0 -25 Td\n");
        sb.append('(').append(escapePdfText(joinForPdf(headers))).append(") Tj\n");

        // Línea divisoria
        sb.append("0 -10 Td\n");
        sb.append("(---------------------------------------------) Tj\n");

        // Filas de datos
        sb.append("/F1 10 Tf\n");
        for (List<String> row : rows) {
            sb.append("0 -16 Td\n");
            sb.append('(').append(escapePdfText(joinForPdf(row))).append(") Tj\n");
        }

        // Marco inferior
        sb.append("0 -10 Td\n");
        sb.append("(=============================================) Tj\n");

        sb.append("ET");
        return sb.toString();
    }

    // ===========================
    // MÉTODOS DE AYUDA
    // ===========================
    private static String joinRow(List<String> values) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < values.size(); i++) {
            if (i > 0) sb.append('\t');
            sb.append(escapeExcelField(values.get(i)));
        }
        return sb.toString();
    }

    private static String escapeExcelField(String value) {
        if (value == null) return "";
        String trimmed = value.replace('\t', ' ').replace('\r', ' ').replace('\n', ' ');
        if (trimmed.startsWith("=") || trimmed.startsWith("+") || trimmed.startsWith("-") || trimmed.startsWith("@"))
            return "'" + trimmed;
        return trimmed;
    }

    private static String joinForPdf(List<String> values) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < values.size(); i++) {
            if (i > 0) sb.append("   •   ");
            sb.append(values.get(i) != null ? values.get(i) : "");
        }
        return sb.toString();
    }

    private static String escapePdfText(String text) {
        if (text == null) return "";
        // Reemplazamos caracteres conflictivos pero dejamos los acentos intactos
        return text.replace("\\", "\\\\").replace("(", "\\(").replace(")", "\\)");
    }

    private static void write(ByteArrayOutputStream out, String value) throws IOException {
        // También usamos ISO-8859-1 aquí
        out.write(value.getBytes(StandardCharsets.ISO_8859_1));
    }
}
