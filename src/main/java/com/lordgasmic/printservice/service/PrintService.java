package com.lordgasmic.printservice.service;

import com.google.gson.Gson;
import com.lordgasmic.printservice.models.PrintPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class PrintService {

    private static final String PRINTER_IP = "172.16.0.31";
    private static final int PRINTER_PORT = 9100;

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

        printReceipt(payload.getProperties());
    }

    public void printReceipt(final Map<String, List<String>> receiptContent) {
        try (final Socket socket = new Socket(PRINTER_IP, PRINTER_PORT);
             final OutputStream os = socket.getOutputStream()) {

            final StringBuilder sb = new StringBuilder();

            final String initialize = "\u001B@";
            final String defaultCharacterSize = "\u001D!\u0000";
            final String centerAlign = "\u001Ba\u0001";
            final String leftAlign = "\u001Ba\u0000";
            final String titleCharacterSize = "\u001D!\u0012";
            final String cut = "\u001DVA\u0000";

            final Instant now = Instant.now();
            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("America/Detroit"));

            sb.append(initialize);
            sb.append(centerAlign);
            sb.append(titleCharacterSize);
            sb.append("Lordgasmic\nOrdering").append(System.lineSeparator());
            sb.append(defaultCharacterSize);
            sb.append(leftAlign);
            sb.append("\u001B32\n").append("\u001B30");
            sb.append(formatter.format(now));
            sb.append(System.lineSeparator());
            sb.append("Source: Online Order");
            sb.append(System.lineSeparator());
            sb.append(System.lineSeparator());
            sb.append("Items").append(System.lineSeparator());
            sb.append(centerAlign);
            sb.append("------------------------------------------").append(System.lineSeparator());
            sb.append(leftAlign);

            for (final Map.Entry<String, List<String>> entry : receiptContent.entrySet()) {
                sb.append(entry.getKey()).append(System.lineSeparator());
                for (final String s : entry.getValue()) {
                    sb.append("  - ").append(s).append(System.lineSeparator());
                }
                sb.append(System.lineSeparator());
            }

            sb.append(System.lineSeparator());
            sb.append(cut);

            os.write(sb.toString().getBytes());
            os.flush();
        } catch (final IOException e) {
            log.error("LGC-43F5D1D6-E829-4563-84C5-2F6A08EE81D4: Error printing to thermal printer: {}", e.getMessage());
        }
    }
}
