import com.google.gson.JsonObject;

public class Converter {
    private CurrencyConverterAPI api;

    public Converter(CurrencyConverterAPI api) {
        this.api = api;
    }

    public double convert(double amount, String fromCurrency, String toCurrency) throws Exception {
        return api.getExchangeRate(fromCurrency, toCurrency, amount);
    }

    public JsonObject getLatestRates(String baseCurrency) throws Exception {
        return api.getLatestRates(baseCurrency);
    }
}