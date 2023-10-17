package by.zhukovsky;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;

public class ExchangeRatesApp {
    private static final Logger logger = LoggerFactory.getLogger(ExchangeRatesApp.class);

    private final ActionsService actionsService;

    public ExchangeRatesApp() {
        this.actionsService = new ActionsService();
    }

    public void start() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Welcome to the ExchangeRates app!");
            System.out.println("***********************************");
            System.out.println("List of available commands:");
            System.out.println("\t1. List all currency exchange rates for today.");
            System.out.println("\t2. List all currency exchange rates for a specific date.");
            System.out.println("\t3. List all currency exchange rates for a specific month.");
            System.out.println("\t4. Get today's exchange rate for a specific currency by its abbreviation.");
            System.out.println("\t5. Convert currency to BYN.");
            System.out.println("\t6. Help");
            System.out.println("\t7. Exit");

            String command;
            boolean isRunning = true;

            while (isRunning) {
                System.out.println("***********************************");
                System.out.println("Please enter a command or type \"exit\" to close the app.");
                command = reader.readLine();

                switch (command) {
                    case "1" -> executeGetAllCurrencies();
                    case "2" -> executeGetAllCurrenciesByDate(reader);
                    case "3" -> executeGetAllCurrenciesByMonth(reader);
                    case "4" -> executeGetCurrencyInfoByAbbreviation(reader);
                    case "5" -> executeConvertToBYN(reader);
                    case "6" -> executeHelp();
                    case "7" -> isRunning = false;
                    default -> System.err.println("ERROR: Unknown command. Please try again!");
                }
            }
        } catch (IOException e) {
            logger.error("Error during application startup", e);
            System.err.println("ERROR: Something went wrong while starting the application.");
        }
    }

    private void executeGetAllCurrencies() {
        actionsService.getAllCurrencies();
    }

    private void executeGetAllCurrenciesByDate(BufferedReader reader) throws IOException {
        System.out.println("Please enter the date in the following format \"yyyy-MM-dd\" (e.g., 2022-11-01 or 2022-10-15) and press ENTER");
        System.out.print("Date: ");
        String date = reader.readLine();
        if (ActionsService.isDateFormatValid(date)) {
            actionsService.getAllCurrenciesByDate(date);
            System.out.println();
        } else {
            System.err.println("ERROR: Incorrect date format!");
        }
    }

    private void executeGetAllCurrenciesByMonth(BufferedReader reader) throws IOException {
        System.out.println("Please enter the date in the following format \"yyyy-MM\" and press ENTER");
        System.out.print("Date: ");
        String date = reader.readLine();
        if (ActionsService.isMonthFormatValid(date)) {
            actionsService.getAllCurrenciesByMonth(date);
            System.out.println();
        } else {
            System.err.println("ERROR: Incorrect date format!");
        }
    }

    private void executeGetCurrencyInfoByAbbreviation(BufferedReader reader) throws IOException {
        System.out.println("Please enter a currency abbreviation (e.g., USD, RUB) and press ENTER");
        System.out.print("Currency: ");
        String currency = reader.readLine();
        actionsService.getCurrencyInfoByItsAbbreviation(currency);
        System.out.println();
    }

    private void executeConvertToBYN(BufferedReader reader) throws IOException {
        System.out.println("Please enter a currency abbreviation (e.g., USD, RUB) you wish to sell");
        System.out.print("Currency: ");
        String currency = reader.readLine();
        System.out.print("Enter the money amount: ");
        BigDecimal amount = new BigDecimal(reader.readLine());

        System.out.printf("Convert %.2f %s to BYN? (yes/no): ", amount, currency);
        String confirmation = reader.readLine();

        if (confirmation.equalsIgnoreCase("yes")) {
            actionsService.convertToBYN(currency, amount);
            System.out.println();
        } else {
            System.out.println("Conversion canceled.");
        }
    }

    private void executeHelp() {
        System.out.println("Available Commands:");
        System.out.println("1. List all currency exchange rates for today.");
        System.out.println("2. List all currency exchange rates for a specific date.");
        System.out.println("3. List all currency exchange rates for a specific month.");
        System.out.println("4. Get today's exchange rate for a specific currency by its abbreviation.");
        System.out.println("5. Convert currency to BYN.");
        System.out.println("6. Help");
        System.out.println("7. Exit");
    }


    public static void main(String[] args) {
        ExchangeRatesApp app = new ExchangeRatesApp();
        app.start();
    }
}