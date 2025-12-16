package com.lordgasmic.printservice.printers;

import com.lordgasmic.printservice.models.GroceryListPayload;

public class GroceryListPrinter extends Printer{

    private final GroceryListPayload payload;

    public GroceryListPrinter(final GroceryListPayload payload) {
        super();

        this.payload = payload;
    }

    @Override
    protected String print() {
        return "";
    }
}
