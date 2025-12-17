package com.lordgasmic.printservice.printers;

import com.google.gson.Gson;
import com.lordgasmic.printservice.models.GroceryListPayload;
import com.lordgasmic.printservice.models.Payload;
import com.lordgasmic.printservice.models.ReceiptPayload;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.Setter;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

@Setter
public abstract class Printer {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(Printer.class);

    protected static final String PRINTER_IP = "172.16.0.31";
    protected static final int PRINTER_PORT = 9100;

    protected MeterRegistry meterRegistry;
    protected Gson gson;

    public Printer() {
        gson = new Gson();
    }

    public static Printer getPrinter(final String message) {
        final Gson mGson = new Gson();
        try {
            final Payload payload = mGson.fromJson(message, Payload.class);
            switch (payload.getType()) {
                case RECEIPT -> {
                    return new ReceiptPrinter(mGson.fromJson(message, ReceiptPayload.class));
                }
                case NOTIFICATION -> throw new UnsupportedOperationException("NOTIFICATION not supported yet.");
                case FETCH -> throw new UnsupportedOperationException("FETCH not supported yet.");
                case GROCERY_LIST -> {
                    return new GroceryListPrinter(mGson.fromJson(message, GroceryListPayload.class));
                }
                default -> throw new UnsupportedOperationException("Unknown type.");
            }
        } catch (final ClassCastException e) {
            log.error(e.getMessage());
            log.error("Could not cast class", e);
        }

        throw new RuntimeException("Casting derped");
    }

    public void doPrint() {
        meterRegistry.counter("print-service.print-requests.total").increment();

        try (final Socket socket = new Socket(PRINTER_IP, PRINTER_PORT);
             final OutputStream os = socket.getOutputStream()) {

            final String content = print();

            os.write(content.getBytes());
            os.flush();

            meterRegistry.counter("print-service.print-requests.success").increment();
        } catch (final IOException e) {
            meterRegistry.counter("print-service.print-requests.failed").increment();
            log.error("LGC-43F5D1D6-E829-4563-84C5-2F6A08EE81D4: Error printing to thermal printer: {}", e.getMessage());
        }
    }

    protected abstract String print();
}
