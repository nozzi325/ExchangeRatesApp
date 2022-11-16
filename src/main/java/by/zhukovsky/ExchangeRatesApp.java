package by.zhukovsky;

import java.io.*;
import java.math.BigDecimal;

public class ExchangeRatesApp {

    public void start(){
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))){
            System.out.println("Welcome to the ExchangeRates app!");
            System.out.println("***********************************");
            System.out.println("List of available commands:\n" +
                    "\t1. Receive all currencies exchange rates on today's date.\n" +
                    "\t2. Receive all currencies exchange rates for currencies set daily.\n" +
                    "\t3. Receive all currencies exchange rates for currencies set monthly.\n" +
                    "\t4. Receive today's exchange rate for certain currency by its abbreviation.\n" +
                    "\t5. Convert currency to BYN.");
            String command;
            while (true){
                    System.out.println("***********************************");
                    System.out.println("Please, enter command or \"exit\" to close the app");
                    command = reader.readLine();
                    switch (command){
                        case "1":
                            Actions.getAllCurrencies();
                            System.out.println();
                            break;
                        case "2":
                            System.out.println("Please, enter the date in the following format \"yyyy-mm-dd\" (i.e, 2022-11-01 or 2022-10-15) " +
                                    "and press ENTER");
                            System.out.print("Date: ");
                            Actions.getAllCurrenciesByDate(reader.readLine());
                            System.out.println();
                            break;
                        case "3":
                            System.out.println("Please, enter the date in the following format \"yyyy-mm\" and press ENTER");
                            System.out.print("Date: ");
                            Actions.getAllCurrenciesByMonth(reader.readLine());
                            System.out.println();
                            break;
                        case "4":
                            System.out.println("Please, enter currency abbreviation (i.e USD, RUB) and press ENTER");
                            System.out.print("Currency: ");
                            Actions.getCurrencyInfoByItsAbbreviation(reader.readLine());
                            System.out.println();
                            break;
                        case "5":
                            System.out.println("Please, enter currency abbreviation (i.e USD, RUB) you wish " +
                                            "to sell");
                            System.out.print("Currency: ");
                            String currency = reader.readLine();
                            System.out.print("Enter money amount: ");
                            BigDecimal amount = new BigDecimal(reader.readLine());
                            Actions.convertToBYN(currency,amount);
                            System.out.println();
                            break;
                        case "exit":
                            return;
                        default:
                            System.err.println("ERROR: Unknown command, please try again!");
                            break;
                    }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
