import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CurrencyConverterAPI {
    private final String apiKey;
    private final HttpClient httpClient;
    private final String baseUrl = "https://v6.exchangerate-api.com/v6/";
    private final Gson gson;

    public CurrencyConverterAPI(String apiKey) {
        this.apiKey = apiKey;
        this.httpClient = HttpClient.newHttpClient();
        this.gson = new Gson();
    }

    public double getExchangeRate(String fromCurrency, String toCurrency, double amount) throws Exception {
        String url = baseUrl + apiKey + "/pair/" + fromCurrency + "/" + toCurrency + "/" + amount;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            JsonObject jsonResponse = gson.fromJson(response.body(), JsonObject.class);
            return jsonResponse.get("conversion_result").getAsDouble();
        } else {
            handleApiError(response.body());
            return 0; // This line will never be reached due to exception throwing in handleApiError
        }
    }

    public JsonObject getLatestRates(String baseCurrency) throws Exception {
        String url = baseUrl + apiKey + "/latest/" + baseCurrency;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return gson.fromJson(response.body(), JsonObject.class);
        } else {
            handleApiError(response.body());
            return null; // This line will never be reached due to exception throwing in handleApiError
        }
    }

    private void handleApiError(String errorResponse) throws Exception {
        JsonObject error = gson.fromJson(errorResponse, JsonObject.class);
        String errorType = error.get("error-type").getAsString();
        switch (errorType) {
            case "unsupported-code":
                throw new Exception("Código de moneda no soportado.");
            case "malformed-request":
                throw new Exception("Solicitud mal formada.");
            case "invalid-key":
                throw new Exception("Clave API inválida.");
            case "inactive-account":
                throw new Exception("Cuenta inactiva. Por favor, confirme su dirección de correo electrónico.");
            case "quota-reached":
                throw new Exception("Se ha alcanzado el límite de solicitudes para su plan.");
            default:
                throw new Exception("Error desconocido: " + errorType);
        }
    }
}