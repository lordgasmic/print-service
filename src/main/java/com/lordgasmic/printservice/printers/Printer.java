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

public abstract class Printer {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(Printer.class);

    protected static final String PRINTER_IP = "172.16.0.31";
    protected static final int PRINTER_PORT = 9100;

    protected static final String initialize = "\u001B@";
    protected static final String leftAlign = "\u001Ba\u0000";
    protected static final String centerAlign = "\u001Ba\u0001";
    protected static final String defaultCharacterSize = "\u001D!\u0000";
    protected static final String titleCharacterSize = "\u001D!\u0012";
    protected static final String cut = "\u001DVA\u0000";
    protected static final String oneSixthInchLineSpacing = "\u001B32";
    protected static final String oneEighthInchLineSpacing = "\u001B30";

    protected MeterRegistry meterRegistry;
    protected Gson gson;

    public Printer( MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        gson = new Gson();
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
