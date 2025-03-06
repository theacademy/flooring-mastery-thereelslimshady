package org.example.dao;

import org.example.model.Product;
import org.example.model.Tax;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.*;

@Component
public class ProductDaoFileImpl implements ProductDao{
    Map<String, Product> productMap = new HashMap<>();
    private final String FILEPATH ;
    private final String DELIMITER = ",";

    public ProductDaoFileImpl(String filepath) {
        this.FILEPATH = filepath;
    }

    public ProductDaoFileImpl() {
        this.FILEPATH = "Data/Products.txt";
    }

    public Map<String, Product> getProductMap() {
        return productMap;
    }

    @Override
    public void load() throws ProductDataPersistanceException {
        Scanner scanner;

        try{
            scanner = new Scanner(new BufferedReader(new FileReader(FILEPATH)));
        }catch (FileNotFoundException e){
            throw new ProductDataPersistanceException(
                    "Cannot find products data file", e);
        }
        String currentLine;
        Product currentProduct;
        scanner.nextLine();
        while (scanner.hasNextLine()){
            currentLine = scanner.nextLine();
            currentProduct = unmarshallProduct(currentLine);
            productMap.put(currentProduct.getProductType().toUpperCase(), currentProduct);
        }
        scanner.close();

    }

    public Product unmarshallProduct(String line){
        String[] input = line.split(DELIMITER);
        return new Product(input[0].trim(), new BigDecimal(input[1].trim()), new BigDecimal(input[2].trim()));
    }

    @Override
    public List<Product> getAllProducts() throws ProductDataPersistanceException {
        load();
        return new ArrayList<>(productMap.values());
    }

}
