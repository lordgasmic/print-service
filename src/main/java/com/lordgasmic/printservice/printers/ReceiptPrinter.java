package com.lordgasmic.printservice.printers;

import com.lordgasmic.printservice.models.ReceiptPayload;
import com.lordgasmic.printservice.models.ReceiptPrinterOptions;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class ReceiptPrinter extends Printer {

    private final ReceiptPayload payload;

    public ReceiptPrinter(final ReceiptPayload payload) {
        super();

        this.payload = payload;
    }

    @Override
    protected String print() {
        meterRegistry.counter("print-service.print-requests.receipt.total").increment();

        final StringBuilder sb = new StringBuilder();

        final String initialize = "\u001B@";
        final String leftAlign = "\u001Ba\u0000";
        final String centerAlign = "\u001Ba\u0001";
        final String defaultCharacterSize = "\u001D!\u0000";
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

        for (final Map.Entry<ReceiptPrinterOptions, String[]> entry : payload.getProperties().entrySet()) {
            meterRegistry.counter("print-service.print-requests."+ entry.getKey().toString()).increment();
            sb.append(entry.getKey().getValue()).append(System.lineSeparator());
            for (final String s : entry.getValue()) {
                sb.append("  - ").append(s).append(System.lineSeparator());
            }
            sb.append(System.lineSeparator());
        }

        sb.append(System.lineSeparator());
        sb.append(cut);

        return sb.toString();
    }
}
