# Exchange-Rates App

This console application utilizes the NBRB API to fetch the official exchange rates of the Belarusian Ruble against foreign currencies.

## Features

- Retrieve exchange rates for all currencies for the current date.
- Retrieve exchange rates for currencies set daily by specifying a date.
- Retrieve exchange rates for currencies set monthly by specifying a year and month.
- Obtain the exchange rate of a specific currency for the current date by its abbreviation.
- Perform currency conversion to Belarusian Ruble (BYN).

## Technologies

- Java 18
- Jackson
- ASCII Table (for tabular data presentation)
- Logging with SLF4J and Logback

## Description

This educational project aims to demonstrate how to build a simple console application that interacts with a web API, parses JSON responses, and provides various currency-related functionalities. The application includes SLF4J and Logback for efficient logging, capturing errors and information during its execution.

## Usage

You can run the application by executing the `ExchangeRatesApp` class from your IDE or by running the compiled JAR file.

### Commands

The application supports the following commands:

1. `1`: Retrieve exchange rates for all currencies for the current date.
2. `2`: Retrieve exchange rates for currencies set daily by specifying a date.
3. `3`: Retrieve exchange rates for currencies set monthly by specifying a year and month.
4. `4`: Obtain the exchange rate of a specific currency for the current date by its abbreviation.
5. `5`: Perform currency conversion to Belarusian Ruble (BYN).
6. `6`: Help
7. `7`: Exit the application.

Make sure to follow the on-screen instructions for each command.

## Building and Running

1. Clone this repository to your local machine.
2. Open the project in your Java IDE.
3. Build the project.
4. Run the `ExchangeRatesApp` class.

