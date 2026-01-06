package com.lordgasmic.printservice.printers;

import com.google.gson.Gson;
import com.lordgasmic.printservice.models.GroceryListPayload;
import com.lordgasmic.printservice.models.Payload;
import com.lordgasmic.printservice.models.ReceiptPayload;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class PrinterFactory {

    protected MeterRegistry meterRegistry;

    public PrinterFactory(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public   Printer getPrinter(String message) {
        final Gson mGson = new Gson();
        final Payload payload = mGson.fromJson(message, Payload.class);
        switch (payload.getType()) {
            case RECEIPT -> {
                return new ReceiptPrinter(meterRegistry,message);
            }
            case NOTIFICATION -> throw new UnsupportedOperationException("NOTIFICATION not supported yet.");
            case FETCH -> throw new UnsupportedOperationException("FETCH not supported yet.");
            case GROCERY_LIST -> {
                return new GroceryListPrinter(meterRegistry ,message);
            }
            default -> throw new UnsupportedOperationException("Unknown type.");
        }
    }
}
