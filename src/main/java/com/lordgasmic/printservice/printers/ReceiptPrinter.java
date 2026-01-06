package com.lordgasmic.printservice.printers;

import com.lordgasmic.printservice.models.GroceryListPayload;
import com.lordgasmic.printservice.models.ReceiptPayload;
import io.micrometer.core.instrument.MeterRegistry;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class ReceiptPrinter extends Printer {

    private final ReceiptPayload payload;

    public ReceiptPrinter(MeterRegistry meterRegistry, final String message) {
        super(meterRegistry);

        this.payload = gson.fromJson(message, ReceiptPayload.class);
    }

    @Override
    protected String print() {
        meterRegistry.counter("print-service.print-requests.receipt.total").increment();

        final StringBuilder sb = new StringBuilder();

        final Instant now = Instant.now();
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("America/Detroit"));

        sb.append(initialize);
        sb.append(centerAlign);
        sb.append(titleCharacterSize);
        sb.append("Lordgasmic\nOrdering").append(System.lineSeparator());
        sb.append(defaultCharacterSize);
        sb.append(leftAlign);
        sb.append(oneSixthInchLineSpacing);
        sb.append(System.lineSeparator());
        sb.append(oneEighthInchLineSpacing);
        sb.append(formatter.format(now));
        sb.append(System.lineSeparator());
        sb.append("Source: Online Order");
        sb.append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append("Items").append(System.lineSeparator());
        sb.append(centerAlign);
        sb.append("------------------------------------------").append(System.lineSeparator());
        sb.append(leftAlign);

        for (final Map.Entry<String, String[]> entry : payload.getProperties().entrySet()) {
            sb.append(entry.getKey()).append(System.lineSeparator());
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
