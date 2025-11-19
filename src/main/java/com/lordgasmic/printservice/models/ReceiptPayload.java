package com.lordgasmic.printservice.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
public class ReceiptPayload extends Payload {
    private String message;
    private Map<String, List<String>> properties;
}
