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
    private final String FILEPATH = "Data/Products.txt";
    private final String DELIMITER = ",";

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
            productMap.put(currentProduct.getProductType(), currentProduct);
        }
        scanner.close();

    }

    public Product unmarshallProduct(String line){
        String[] input = line.split(DELIMITER);
        return new Product(input[0].trim(), new BigDecimal(input[1].trim()), new BigDecimal(input[2].trim()));   //ignore CASE!!
    }

    @Override
    public List<Product> getAllProducts() throws ProductDataPersistanceException {
        load();
        return new ArrayList<>(productMap.values());
    }

    @Override
    public Product getByProductType(String productType) {
        return null;
    }
}
