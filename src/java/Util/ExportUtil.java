package Util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
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
        write(pdf, "3 0 obj\n<< /Type /Page /Parent 2 0 R /MediaBox [0 0 595 842] "
                + "/Contents 4 0 R /Resources << /Font << /F1 5 0 R >> >> >>\nendobj\n");

        String content = buildContentStream(title, headers, rows);
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

    private static String buildContentStream(String title, List<String> headers, List<List<String>> rows) {
        StringBuilder sb = new StringBuilder();
        float pageWidth = 595f;
        float pageHeight = 842f;
        float margin = 36f;
        float titleFontSize = 18f;
        float headerFontSize = 11f;
        float cellFontSize = 10f;
        float headerLineHeight = headerFontSize + 4f;
        float cellLineHeight = cellFontSize + 4f;
        float cellPadding = 6f;

       float tableWidth = pageWidth - (2 * margin);
        int columnCount = headers != null ? headers.size() : 0;
        if (columnCount == 0) {
            columnCount = 1;
            headers = Collections.singletonList("");
        }
        float columnWidth = tableWidth / columnCount;
        float[] columnPositions = new float[columnCount];
        float[] columnWidths = new float[columnCount];
        for (int i = 0; i < columnCount; i++) {
            columnPositions[i] = margin + (i * columnWidth);
            columnWidths[i] = columnWidth;
        }

        float cursorY = pageHeight - margin;

        appendText(sb, titleFontSize, margin, cursorY, title);
        cursorY -= (titleFontSize + 12f);

        sb.append("1 w\n");

        List<List<String>> headerLines = new ArrayList<>();
        for (int i = 0; i < columnCount; i++) {
            String header = headers.get(i);
            headerLines.add(wrapText(header, columnWidths[i] - (2 * cellPadding), headerFontSize));
        }
        float headerHeight = calculateRowHeight(headerLines, headerLineHeight, cellPadding);

        sb.append("q\n");
        sb.append(String.format(Locale.US, "0.9 0.9 0.9 rg\n%.2f %.2f %.2f %.2f re f\n", margin,
                cursorY - headerHeight, tableWidth, headerHeight));
        sb.append("Q\n");

        drawRow(sb, headerLines, columnPositions, columnWidths, cursorY, headerHeight, headerFontSize,
                headerLineHeight, cellPadding);
        cursorY -= headerHeight;

        if (rows != null) {
            for (List<String> row : rows) {
                List<List<String>> cellLines = new ArrayList<>();
                for (int i = 0; i < columnCount; i++) {
                    String value = row != null && i < row.size() ? row.get(i) : "";
                    cellLines.add(wrapText(value, columnWidths[i] - (2 * cellPadding), cellFontSize));
                }
                float rowHeight = calculateRowHeight(cellLines, cellLineHeight, cellPadding);
                drawRow(sb, cellLines, columnPositions, columnWidths, cursorY, rowHeight, cellFontSize,
                        cellLineHeight, cellPadding);
                cursorY -= rowHeight;
            }
        }
        return sb.toString();
    }

   private static void appendText(StringBuilder sb, float fontSize, float x, float y, String text) {
        sb.append("BT\n");
        sb.append(String.format(Locale.US, "/F1 %.2f Tf\n", fontSize));
        sb.append(String.format(Locale.US, "1 0 0 1 %.2f %.2f Tm\n", x, y));
        sb.append('(').append(escapePdfText(text)).append(") Tj\n");
        sb.append("ET\n");
    }

    private static float calculateRowHeight(List<List<String>> cellLines, float lineHeight, float padding) {
        int maxLines = 1;
        for (List<String> lines : cellLines) {
            if (lines != null && lines.size() > maxLines) {
                maxLines = lines.size();
            }
            
        }
         return (2 * padding) + (maxLines * lineHeight);
    }
    
     private static void drawRow(StringBuilder sb, List<List<String>> cellLines, float[] columnPositions,
            float[] columnWidths, float rowTopY, float rowHeight, float fontSize, float lineHeight,
            float padding) {
        int columnCount = columnPositions.length;
        for (int i = 0; i < columnCount; i++) {
            float x = columnPositions[i];
            float width = columnWidths[i];
            sb.append(String.format(Locale.US, "%.2f %.2f %.2f %.2f re S\n", x, rowTopY - rowHeight, width,
                    rowHeight));

            List<String> lines = cellLines.get(i);
            if (lines == null || lines.isEmpty()) {
                lines = Collections.singletonList("");
            }
            for (int lineIndex = 0; lineIndex < lines.size(); lineIndex++) {
                float textX = x + padding;
                float textY = rowTopY - padding - fontSize - (lineIndex * lineHeight);
                sb.append("BT\n");
                sb.append(String.format(Locale.US, "/F1 %.2f Tf\n", fontSize));
                sb.append(String.format(Locale.US, "1 0 0 1 %.2f %.2f Tm\n", textX, textY));
                sb.append('(').append(escapePdfText(lines.get(lineIndex))).append(") Tj\n");
                sb.append("ET\n");
            }
        }
    }

    private static List<String> wrapText(String text, float availableWidth, float fontSize) {
        List<String> lines = new ArrayList<>();
        if (text == null || text.isEmpty()) {
            lines.add("");
            return lines;
        }

        float safeWidth = Math.max(1f, availableWidth);
        int maxCharsPerLine = Math.max(1, (int) Math.floor(safeWidth / (fontSize * 0.5f)));

        String normalized = text.replace('\r', ' ').replace('\n', ' ');
        String trimmed = normalized.trim();
        if (trimmed.isEmpty()) {
            lines.add("");
            return lines;
        }

        String[] words = trimmed.split("\\s+");

        StringBuilder current = new StringBuilder();
        for (String word : words) {
            if (word.length() > maxCharsPerLine) {
                if (current.length() > 0) {
                    lines.add(current.toString());
                    current.setLength(0);
                }
                int index = 0;
                while (index < word.length()) {
                    int end = Math.min(word.length(), index + maxCharsPerLine);
                    lines.add(word.substring(index, end));
                    index = end;
                }
                continue;
            }

            if (current.length() == 0) {
                current.append(word);
            } else if (current.length() + 1 + word.length() <= maxCharsPerLine) {
                current.append(' ').append(word);
            } else {
                lines.add(current.toString());
                current.setLength(0);
                current.append(word);
            }
        }

        if (current.length() > 0) {
            lines.add(current.toString());
        }

        if (lines.isEmpty()) {
            lines.add("");
        }

        return lines;
    }

    private static String escapePdfText(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("\\", "\\\\").replace("(", "\\(").replace(")", "\\)");
    }

    private static void write(ByteArrayOutputStream out, String value) throws IOException {
          out.write(value.getBytes(StandardCharsets.ISO_8859_1));
    }
}