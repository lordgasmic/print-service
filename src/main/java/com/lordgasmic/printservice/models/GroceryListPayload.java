package com.lordgasmic.printservice.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
public class GroceryListPayload extends Payload {
    private Map<String, List<Item>> groceryList;
}
