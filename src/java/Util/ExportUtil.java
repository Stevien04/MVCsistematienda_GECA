package Util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

/**
 * Utilidad sencilla para exportar datos tabulares a Excel (TSV) y a un PDF básico
 * sin dependencias externas.
 */
public final class ExportUtil {

    private ExportUtil() {
    }

    public static void exportToExcel(HttpServletResponse response, String fileName,
            List<String> headers, List<List<String>> rows) throws IOException {

        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        PrintWriter writer = response.getWriter();
        writer.println(joinRow(headers));
        for (List<String> row : rows) {
            writer.println(joinRow(row));
        }
        writer.flush();
    }

    public static void exportToPdf(HttpServletResponse response, String fileName, String title,
            List<String> headers, List<List<String>> rows) throws IOException {

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        byte[] pdfData = buildSimplePdf(title, headers, rows);
        response.setContentLength(pdfData.length);

        OutputStream out = response.getOutputStream();
        out.write(pdfData);
        out.flush();
    }

    private static String joinRow(List<String> values) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < values.size(); i++) {
            if (i > 0) {
                sb.append('\t');
            }
            sb.append(escapeExcelField(values.get(i)));
        }
        return sb.toString();
    }

    private static String escapeExcelField(String value) {
        if (value == null) {
            return "";
        }
        String trimmed = value.replace('\t', ' ').replace('\r', ' ').replace('\n', ' ');
        if (trimmed.startsWith("=") || trimmed.startsWith("+")
                || trimmed.startsWith("-") || trimmed.startsWith("@")) {
            return "'" + trimmed;
        }
        return trimmed;
    }

    private static byte[] buildSimplePdf(String title, List<String> headers, List<List<String>> rows)
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

        String content = buildContentStream(title, headers, rows);
        byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);

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

    private static String buildContentStream(String title, List<String> headers, List<List<String>> rows) {
        StringBuilder sb = new StringBuilder();
        sb.append("BT\n");
        sb.append("/F1 18 Tf\n");
        sb.append("50 780 Td\n");
        sb.append('(').append(escapePdfText(title)).append(") Tj\n");

        sb.append("/F1 12 Tf\n");
        sb.append("0 -30 Td\n");
        sb.append('(').append(escapePdfText(joinForPdf(headers))).append(") Tj\n");

        for (List<String> row : rows) {
            sb.append("0 -18 Td\n");
            sb.append('(').append(escapePdfText(joinForPdf(row))).append(") Tj\n");
        }

        sb.append("ET");
        return sb.toString();
    }

    private static String joinForPdf(List<String> values) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < values.size(); i++) {
            if (i > 0) {
                sb.append("  |  ");
            }
            String value = values.get(i);
            sb.append(value != null ? value : "");
        }
        return sb.toString();
    }

    private static String escapePdfText(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("\\", "\\\\").replace("(", "\\(").replace(")", "\\)");
    }

    private static void write(ByteArrayOutputStream out, String value) throws IOException {
        out.write(value.getBytes(StandardCharsets.UTF_8));
    }
}