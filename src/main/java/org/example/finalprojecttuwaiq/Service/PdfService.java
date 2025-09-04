package org.example.finalprojecttuwaiq.Service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.stereotype.Service;

import org.commonmark.node.*;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class PdfService {
    private static final String OUTPUT_DIR = "generated-pdfs/";

    public String generatePdf(String fileName, String markdownContent) throws IOException {
        Files.createDirectories(Paths.get(OUTPUT_DIR));
        String filePath = OUTPUT_DIR + fileName;

        // Convert markdown → HTML (use commonmark or flexmark library)
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdownContent);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        String html = renderer.render(document);

        try (OutputStream os = new FileOutputStream(filePath)) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(html, null);
            builder.toStream(os);
            builder.run();
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }
    return filePath;
    }
}
