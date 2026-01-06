package com.lordgasmic.printservice.printers;

import com.lordgasmic.printservice.models.GroceryListPayload;
import com.lordgasmic.printservice.models.Item;
import io.micrometer.core.instrument.MeterRegistry;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class GroceryListPrinter extends Printer {

    private final GroceryListPayload payload;

    public GroceryListPrinter(MeterRegistry meterRegistry, String message) {
        super(meterRegistry);

        this.payload = gson.fromJson(message, GroceryListPayload.class);
    }

    @Override
    protected String print() {
        meterRegistry.counter("print-service.print-requests.grocery-list.total").increment();

        final StringBuilder sb = new StringBuilder();

        final Instant now = Instant.now();
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("America/Detroit"));

        sb.append(initialize);
        sb.append(centerAlign);
        sb.append(titleCharacterSize);
        sb.append("Lordgasmic\nFood\nLibrary").append(System.lineSeparator());
        sb.append(defaultCharacterSize);
        sb.append(leftAlign);
        sb.append(oneSixthInchLineSpacing);
        sb.append(System.lineSeparator());
        sb.append(oneEighthInchLineSpacing);
        sb.append(formatter.format(now));
        sb.append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append("Items").append(System.lineSeparator());
        sb.append(centerAlign);
        sb.append("------------------------------------------").append(System.lineSeparator());
        sb.append(leftAlign);

        for (final Map.Entry<String, List<Item>> entry : payload.getGroceryList().entrySet()) {
            sb.append(entry.getKey()).append(System.lineSeparator());
            for (final Item item : entry.getValue()) {
                sb.append("  - ").append(item.quantity()).append(" ").append(item.item()).append(System.lineSeparator());
            }
            sb.append(System.lineSeparator());
        }

        sb.append(System.lineSeparator());
        sb.append(cut);

        return sb.toString();
    }
}
