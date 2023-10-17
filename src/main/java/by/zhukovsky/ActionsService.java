package by.zhukovsky;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;
import com.github.freva.asciitable.HorizontalAlign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

public class ActionsService {
    private static final Logger logger = LoggerFactory.getLogger(ActionsService.class);
    private static final String BASE_URL = "https://api.nbrb.by/exrates/rates";
    private final HttpClient client;

    public ActionsService() {
        client = HttpClient.newHttpClient();
    }

    public void getAllCurrencies() {
        URI uri = URI.create(BASE_URL + "?periodicity=0");
        List<CurrencyInfo> currencies = receiveAllCurrencies(uri);

        if(currencies == null) return;

        System.out.println("Currencies information on " + LocalDate.now());
        printResults(currencies);
    }

    public void getAllCurrenciesByDate(String date) {
        if (!isDateFormatValid(date)) {
            System.err.println("ERROR: Incorrect date format!");
            return;
        }

        LocalDate providedDate = LocalDate.parse(date);
        if (isDateInFuture(providedDate)) {
            System.err.println("ERROR: Date can't be greater than today! Check the date and try again.");
            return;
        }

        URI uri = URI.create(BASE_URL + "?ondate=" + date + "&periodicity=0");
        List<CurrencyInfo> currencies = receiveAllCurrencies(uri);

        if(currencies == null) return;

        System.out.println("Currencies information on " + date);
        printResults(currencies);
    }

    public void getAllCurrenciesByMonth(String date) {
        if (!isMonthFormatValid(date)) {
            System.err.println("ERROR: Incorrect date format!");
            return;
        }

        LocalDate providedDate = LocalDate.parse(date + "-01");
        if (isDateInFuture(providedDate)) {
            System.err.println("ERROR: Date can't be greater than today! Check the date and try again.");
            return;
        }

        URI uri = URI.create(BASE_URL + "?ondate=" + date + "&periodicity=1");
        List<CurrencyInfo> currencies = receiveAllCurrencies(uri);

        if(currencies == null) return;

        System.out.println("Currencies information on " + date);
        printResults(currencies);
    }

    public void getCurrencyInfoByItsAbbreviation(String name) {
        URI uri = URI.create(BASE_URL + "/" + name + "?parammode=2");
        CurrencyInfo currencyInfo = receiveOneCurrency(uri);

        if (currencyInfo == null) {
            System.err.println("ERROR: Currency with such abbreviation not found! Check abbreviation and try again.");
            return;
        }

        System.out.println("***********************************");
        System.out.println("Currency information on " + LocalDate.now());
        System.out.println("***********************************");
        System.out.println("Currency: " + currencyInfo.getCode());
        System.out.println("Rate: " + currencyInfo.getRate());
        System.out.println("Scale: " + currencyInfo.getScale());
    }

    public void convertToBYN(String currency, BigDecimal amount) {
        URI uri = URI.create(BASE_URL + "/" + currency + "?parammode=2");
        CurrencyInfo currencyInfo = receiveOneCurrency(uri);

        System.out.println(currencyInfo);
        if (currencyInfo == null) {
            System.err.println("ERROR: Currency with such abbreviation not found! Check abbreviation and try again.");
            return;
        }

        BigDecimal result = amount.multiply(currencyInfo.getRate())
                .divide(BigDecimal.valueOf(currencyInfo.getScale()), 2, RoundingMode.HALF_UP);
        System.out.printf("Result: %.2f BYN%n", result);
    }

    private List<CurrencyInfo> receiveAllCurrencies(URI uri) {
        String response = sendHttpRequest(uri);
        if(response == null) {
            return null;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            CurrencyInfo[] currencyList = objectMapper.readValue(response, CurrencyInfo[].class);
            return Arrays.asList(currencyList);
        } catch (JsonProcessingException e) {
            logger.error("Error processing server response", e);
            System.err.println("ERROR: Unable to process server response.");
            return null;
        }
    }

    private CurrencyInfo receiveOneCurrency(URI uri) {
        String response = sendHttpRequest(uri);

        ObjectMapper objectMapper = new ObjectMapper();
        CurrencyInfo currency = null;

        if (response != null) {
            if (response.startsWith("{")) {
                try {
                    currency = objectMapper.readValue(response, CurrencyInfo.class);
                } catch (JsonProcessingException e) {
                    logger.error("Error processing server response", e);
                    System.err.println("ERROR: Unable to process server response.");
                }
            } else {
                System.err.println("ERROR: Currency with such abbreviation not found! Check abbreviation and try again.");
            }
        }

        return currency;
    }

    private String sendHttpRequest(URI uri) {
        HttpRequest request = HttpRequest.newBuilder(uri).build();

        try {
            HttpResponse<String> response = this.client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            logger.error("Error executing HTTP request", e);
            System.err.println("ERROR: Unable to perform HTTP request.");
            return null;
        }
    }

    public static boolean isDateFormatValid(String date) {
        try {
            LocalDate.parse(date);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static boolean isMonthFormatValid(String date) {
        try {
            YearMonth.parse(date);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private boolean isDateInFuture(LocalDate date) {
        return date.isAfter(LocalDate.now());
    }

    private void printResults(List<CurrencyInfo> currencyInfos) {
        System.out.println(AsciiTable.getTable(currencyInfos, Arrays.asList(
                new Column().header("Currency")
                        .headerAlign(HorizontalAlign.CENTER)
                        .dataAlign(HorizontalAlign.LEFT)
                        .with(CurrencyInfo::getCode),
                new Column().header("Rate")
                        .headerAlign(HorizontalAlign.CENTER)
                        .dataAlign(HorizontalAlign.LEFT)
                        .with(x -> String.format("%.4f", x.getRate())),
                new Column().header("Scale")
                        .headerAlign(HorizontalAlign.CENTER)
                        .dataAlign(HorizontalAlign.LEFT)
                        .with(x -> String.format("%d", x.getScale())))));
    }
}
