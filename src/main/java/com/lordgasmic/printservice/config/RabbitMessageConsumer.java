package com.lordgasmic.printservice.config;

import com.lordgasmic.printservice.service.PrintService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMessageConsumer {

    private static final String QUEUE_NAME = "lordgasmic.print-service";

    private final PrintService printService;

    public RabbitMessageConsumer(final PrintService printService) {
        this.printService = printService;
    }

    @RabbitListener(queues = QUEUE_NAME)
    public void receiveMessage(final String message) {
        printService.handleMessage(message);
    }
}
