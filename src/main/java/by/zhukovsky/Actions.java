package by.zhukovsky;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;
import com.github.freva.asciitable.HorizontalAlign;

import java.io.*;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;


public class Actions {
    public static void getAllCurrencies() {
        URI uri = URI.create("https://www.nbrb.by/api/exrates/rates?periodicity=0");

        List<CurrencyInfo> currencies = getAllCurrencies(uri);

        System.out.println("Currencies information on " + LocalDate.now());
        printResults(currencies);
    }

    public static void getAllCurrenciesByDate(String date){
        if (!date.matches("^(199\\d|20\\d{2})-(0[1-9]|10|11|12)-\\d{2}$")){
            System.err.println("ERROR: Incorrect date format!");
            return;
        }

        LocalDate localDate = LocalDate.now();
        LocalDate providedDate = LocalDate.parse(date);
        if (providedDate.isAfter(localDate)){
            System.err.println("ERROR: Date can't be greater than today! Check the date and try again.");
            return;
        }

        URI uri = URI.create("https://www.nbrb.by/api/exrates/rates?ondate=" + date +"&periodicity=0");

        List<CurrencyInfo> currencies = getAllCurrencies(uri);

        System.out.println("Currencies information on " + date);
        printResults(currencies);
    }

    public static void getAllCurrenciesByMonth(String date){
        if (!date.matches("^(199\\d|20\\d{2})-(0[1-9]|10|11|12)$")){
            System.err.println("ERROR: Incorrect date format!");
            return;
        }
        LocalDate localDate = LocalDate.now();
        LocalDate providedDate = LocalDate.parse(date+"-01");
        if (providedDate.isAfter(localDate)){
            System.err.println("ERROR: Date can't be greater than today! Check the date and try again.");
            return;
        }

        URI uri = URI.create("https://www.nbrb.by/api/exrates/rates?ondate=" + date +"&periodicity=1");

        List<CurrencyInfo> currencies = getAllCurrencies(uri);

        System.out.println("Currencies information on " + date);
        printResults(currencies);
    }

    public static void getCurrencyInfoByItsAbbreviation(String name){
        URI uri = URI.create("https://www.nbrb.by/api/exrates/rates/" + name +"?parammode=2");

        CurrencyInfo currencyInfo = getOneCurrency(uri);

        if (currencyInfo == null){
            return;
        }

        System.out.println();
        System.out.println("***********************************");
        System.out.println( "Currency information on " + LocalDate.now());
        System.out.println("***********************************");
        System.out.println("Currency: " + currencyInfo.getCode());
        System.out.println("Rate: " + currencyInfo.getRate());
        System.out.println("Scale: " + currencyInfo.getScale());
    }

    public static void convertToBYN(String currency, BigDecimal amount){
        URI uri = URI.create("https://www.nbrb.by/api/exrates/rates/" + currency +"?parammode=2");

        CurrencyInfo currencyInfo = getOneCurrency(uri);

        if (currencyInfo == null){
            return;
        }

        System.out.printf("Result: %.2f BYN",
                amount.multiply(currencyInfo.getRate()).divide(BigDecimal.valueOf(currencyInfo.getScale())));
        System.out.println();

    }

    static List<CurrencyInfo> getAllCurrencies(URI uri){

        var client = HttpClient.newHttpClient();
        var request = HttpRequest
                .newBuilder(uri)
                .build();

        String response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            var currencyList = objectMapper.readValue(response,CurrencyInfo[].class);
            return Arrays.stream(currencyList).toList();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    static CurrencyInfo getOneCurrency(URI uri){
        var client = HttpClient.newHttpClient();
        var request = HttpRequest
                .newBuilder(uri)
                .build();

        String response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        CurrencyInfo currency = null;
        if (response.startsWith("{")){
            try {
                currency = objectMapper.readValue(response,CurrencyInfo.class);
                return currency;
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.err.println("ERROR: Currency with such abbreviation not found!\n"
                    + "Check abbreviation and try again."
            );
        }
        return currency;
    }

    static void printResults(List<CurrencyInfo> currencyInfos){
        System.out.println(AsciiTable.getTable(currencyInfos, Arrays.asList(
                new Column().header("Currency")
                        .headerAlign(HorizontalAlign.CENTER)
                        .dataAlign(HorizontalAlign.LEFT)
                        .with(x -> x.getCode()),
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
