package org.example.dao;

import org.example.model.Product;
import org.example.model.Tax;

import java.util.List;
import java.util.Map;

public interface ProductDao {

    void load() throws ProductDataPersistanceException;

    List<Product> getAllProducts() throws ProductDataPersistanceException;

    Map<String, Product> getProductMap();
}
