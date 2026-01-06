package com.lordgasmic.printservice.service;

import com.lordgasmic.printservice.printers.Printer;
import com.lordgasmic.printservice.printers.PrinterFactory;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PrintService {

    private final PrinterFactory printerFactory;

    public PrintService(PrinterFactory printerFactory) {
        this.printerFactory = printerFactory;
    }

    public void handleMessage(final String message) {
        log.info("LGC-44B29208-3084-45DD-B47F-8F167A2DB5F5: Received Message: {}", message);

        Printer printer = printerFactory.getPrinter(message);
        printer.doPrint();
    }
}
