package pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfWriter;

import javax.swing.*;
import java.awt.*;
import java.io.FileOutputStream;

public class ExportStudentFeePDF {

    public static void exportSummary(String summaryText, Component parent) {
        try {
            Document document = new Document();
            String fileName = "Income_Summary_" + System.currentTimeMillis() + ".pdf";
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();

            // Add title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("Student Fee Summary Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Add actual summary text
            Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 14);
            Paragraph summary = new Paragraph(summaryText, bodyFont);
            summary.setSpacingBefore(10);
            document.add(summary);

            document.close();
            JOptionPane.showMessageDialog(parent, "✅ PDF exported to:\n" + fileName);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(parent, "❌ Failed to export PDF:\n" + ex.getMessage());
        }
    }
}
