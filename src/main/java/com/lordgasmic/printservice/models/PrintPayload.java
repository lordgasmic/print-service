package com.lordgasmic.printservice.models;

import lombok.Data;

import java.util.Map;

@Data
public class PrintPayload {
    private String message;
    private Map<String, String> properties;
}
