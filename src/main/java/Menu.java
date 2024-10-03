import com.google.gson.JsonObject;

import java.util.Scanner;

public class Menu {
    private Scanner scanner;
    private Converter converter;

    public Menu(Converter converter) {
        this.scanner = new Scanner(System.in);
        this.converter = converter;
    }

    public void start() {
        boolean exit = false;
        while (!exit) {
            System.out.println("""
                        --- Conversor de Monedas ---
                        Seleccione una opcion
                        1. Convertir monedas
                        2. Ultimas tasas de la moneda
                        3. Salir
                        """);

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            switch (choice) {
                case 1:
                    showConversionSubMenu();
                    break;
                case 2:
                    showLatestRates();
                    break;
                case 3:
                    exit = true;
                    System.out.println("Gracias por usar el conversor de monedas. ¡Hasta luego!");
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, intente de nuevo.");
            }
        }
    }

    private void showConversionSubMenu() {
        boolean backToMainMenu = false;
        while (!backToMainMenu) {
            System.out.println("""
                        *** Escriba el número de la opción deseada ***");
                        1. EUR a COP
                        2. COP a EUR
                        3. USD a COP
                        4. COP a USD
                        5. USD a EUR
                        6. EUR a USD
                        7. OTRO
                        8. Volver al menú principal
                        """);

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            switch (choice) {
                case 1:
                    convertCurrency("EUR", "COP");
                    break;
                case 2:
                    convertCurrency("COP", "EUR");
                    break;
                case 3:
                    convertCurrency("USD", "COP");
                    break;
                case 4:
                    convertCurrency("COP", "USD");
                    break;
                case 5:
                    convertCurrency("USD", "EUR");
                    break;
                case 6:
                    convertCurrency("EUR", "USD");
                    break;
                case 7:
                    convertCustomCurrencies();
                    break;
                case 8:
                    backToMainMenu = true;
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, intente de nuevo.");
            }
        }
    }

    private void convertCurrency(String fromCurrency, String toCurrency) {
        System.out.print("Ingrese la cantidad a convertir: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consumir el salto de línea

        try {
            double result = converter.convert(amount, fromCurrency, toCurrency);
            System.out.printf("%.2f %s = %.2f %s%n", amount, fromCurrency, result, toCurrency);
        } catch (Exception e) {
            System.out.println("Error al realizar la conversión: " + e.getMessage());
        }
    }

    private void convertCustomCurrencies() {
        System.out.print("Ingrese el código de la moneda de origen (ej. USD): ");
        String fromCurrency = scanner.nextLine().toUpperCase();

        System.out.print("Ingrese el código de la moneda de destino (ej. EUR): ");
        String toCurrency = scanner.nextLine().toUpperCase();

        convertCurrency(fromCurrency, toCurrency);
    }

    private void showLatestRates() {
        System.out.print("Ingrese la moneda base para ver las últimas tasas: ");
        String baseCurrency = scanner.nextLine().toUpperCase();

        try {
            JsonObject rates = converter.getLatestRates(baseCurrency);
            JsonObject conversionRates = rates.getAsJsonObject("conversion_rates");
            System.out.println("Últimas tasas de cambio para " + baseCurrency + ":");
            for (String currency : conversionRates.keySet()) {
                System.out.printf("%s: %.4f%n", currency, conversionRates.get(currency).getAsDouble());
            }
        } catch (Exception e) {
            System.out.println("Error al obtener las últimas tasas: " + e.getMessage());
        }
    }
}