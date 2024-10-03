public class CurrencyConverter {
    public static void main(String[] args) {
        CurrencyConverterAPI api = new CurrencyConverterAPI("282d5ca2e0ea6bcec9637167");
        Converter converter = new Converter(api);
        Menu menu = new Menu(converter);
        menu.start();
    }
}
