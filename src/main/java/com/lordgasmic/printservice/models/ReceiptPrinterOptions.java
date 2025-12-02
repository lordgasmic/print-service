public enum ReceiptPrinterOptions {
  private String value;

WATER("Water"), SALTY_SNACKS("Salty Snacks"),SWEET_SNACKS("Sweet Snacks"), WINE("Wine"),OTHER("Other");

private ReceiptPrinterOptions(String value) {
  this.value = value;
}

public ReceiptPrinterOptions getValue(){
  return value;
}
}
