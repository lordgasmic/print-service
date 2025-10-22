package com.lordgasmic.printservice.models;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class PrintPayload {
    private String message;
    private Map<String, List<String>> properties;
}
