package com.lordgasmic.printservice.service;

import com.google.gson.Gson;
import com.lordgasmic.printservice.models.PrintPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;

@Service
@Slf4j
public class PrintService {

    private final Gson gson;

    public PrintService() {
        gson = new Gson();
    }

    public void handleMessage(final String message) {
        log.info("LGC-44B29208-3084-45DD-B47F-8F167A2DB5F5: Received Message: {}", message);

        final PrintPayload payload = gson.fromJson(message, PrintPayload.class);
        log.info("LGC-D0F6D48A-7E26-4EBE-80F7-F5C6E0D58DBC: Message: {}", payload.getMessage());

        for (final String key : payload.getProperties().keySet()) {
            log.info("LGC-7E24563E-4960-438A-AC85-A90261F1F376: Properties: {}: {}", key, payload.getProperties().get(key));
        }

        final String receipt = "Item 1: $10.00\nItem 2: $5.50\nTotal: $15.50";
        printReceipt(receipt);
    }

    private static final String PRINTER_IP = "172.16.0.32"; // Replace with your printer's IP
    private static final int PRINTER_PORT = 9100; // Standard port for thermal printers

    public void printReceipt(final String receiptContent) {
        try (final Socket socket = new Socket(PRINTER_IP, PRINTER_PORT);
             final OutputStream os = socket.getOutputStream()) {

            // Basic ESC/POS commands (example: center text, bold, cut paper)
            final byte[] centerAlign = {0x1B, 0x61, 0x01}; // ESC a 1 (center alignment)
//            final byte[] boldOn = {0x1B, 0x45, 0x01};    // ESC E 1 (bold on)
//            final byte[] boldOff = {0x1B, 0x45, 0x00};   // ESC E 0 (bold off)
            final byte[] cutPaper = {0x1D, 0x56, 0x00}; // GS V 0 (full cut)
//            final byte[] cutPaper = {0x1D, 0x56, 0x00, 0x30}; // GS V 0 (full cut)

            // Write commands and content
//            os.write(centerAlign);
//            os.write(boldOn);
            os.write("--- Your Store ---".getBytes());
//            os.write(boldOff);
            os.write("\n".getBytes()); // Newline

            os.write(receiptContent.getBytes()); // Your receipt content

            os.write("\n\n\n".getBytes()); // Add some line feeds
            os.write(cutPaper);

            os.flush(); // Ensure all data is sent
            log.info("flushed");
        } catch (final IOException e) {
            log.error("Error printing to thermal printer: {}", e.getMessage());
        }
    }

    public void printReceipt2(final String receiptContent) {
        try (final Socket socket = new Socket(PRINTER_IP, PRINTER_PORT);
             final OutputStream os = socket.getOutputStream()) {
            final PrintStream out = new PrintStream(os);

            // Basic ESC/POS commands (example: center text, bold, cut paper)
            final byte[] centerAlign = {0x1B, 0x61, 0x01}; // ESC a 1 (center alignment)
//            final byte[] boldOn = {0x1B, 0x45, 0x01};    // ESC E 1 (bold on)
//            final byte[] boldOff = {0x1B, 0x45, 0x00};   // ESC E 0 (bold off)
            final byte[] cutPaper = {0x1D, 0x56, 0x00}; // GS V 0 (full cut)
//            final byte[] cutPaper = {0x1D, 0x56, 0x00, 0x30}; // GS V 0 (full cut)

            // Write commands and content
//            os.write(centerAlign);
//            os.write(boldOn);
            out.println("derp");
            out.flush();
            os.write("--- Your Store ---".getBytes());
//            os.write(boldOff);
            os.write("\n".getBytes()); // Newline

            os.write(receiptContent.getBytes()); // Your receipt content

            os.write("\n\n\n".getBytes()); // Add some line feeds
            os.write(cutPaper);

            os.flush(); // Ensure all data is sent
            log.info("flushed");
        } catch (final IOException e) {
            log.error("Error printing to thermal printer: {}", e.getMessage());
        }
    }

    private void printDocument(final String document) {
        try (final Socket socket = new Socket(PRINTER_IP, PRINTER_PORT);
             final OutputStream outputStream = socket.getOutputStream()) {

            final String receipt = "Item 1: $10.00\nItem 2: $5.50\nTotal: $15.50\n";
            final byte[] formFeed = {0x0C};

            // Send the data to be printed
            outputStream.write("Hello from Java!\n\n".getBytes());
            outputStream.write(receipt.getBytes());
            outputStream.write(document.getBytes());
            outputStream.write(formFeed);

        } catch (final IOException e) {
            log.error("Error printing to physical printer: {}", e.getMessage());
        }
    }
}
