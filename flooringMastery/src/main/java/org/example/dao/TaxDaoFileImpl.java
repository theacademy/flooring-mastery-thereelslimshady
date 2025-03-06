package org.example.dao;

import org.example.model.Order;
import org.example.model.Tax;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@Component
public class TaxDaoFileImpl implements TaxDao{
    Map<String, Tax> taxMap = new HashMap<>();
    private final String FILEPATH = "Data/Taxes.txt";
    private final String DELIMITER = ",";

    @Override
    public void load() throws TaxDataPersistanceException {
        Scanner scanner;

        try{
            scanner = new Scanner(new BufferedReader(new FileReader(FILEPATH)));
        }catch (FileNotFoundException e){
            throw new TaxDataPersistanceException(
                    "Cannot find tax data file", e);
        }
        String currentLine;
        Tax currentTax;
        scanner.nextLine();
        while (scanner.hasNextLine()){
            currentLine = scanner.nextLine();
            currentTax = unmarshallTax(currentLine);
            taxMap.put(currentTax.getStateName().toUpperCase(), currentTax);
        }
        scanner.close();

    }

    public Tax unmarshallTax(String line){
        String[] input = line.split(DELIMITER);
        return new Tax(input[0].trim(), input[1].trim(), new BigDecimal(input[2].trim()));   //ignore CASE!!
    }

    @Override
    public Tax getByName(String state) throws TaxDataPersistanceException {
        load();
        return taxMap.get(state.toUpperCase());
    }

    @Override
    public String getNameByAbbr(String abbr) throws TaxDataPersistanceException {
        load();
        return taxMap.values().stream()
                .filter(s -> s.getStateAbbreviation().equalsIgnoreCase(abbr))
                .map(Tax::getStateName)
                .findFirst()
                .orElse(null);
    }
}
