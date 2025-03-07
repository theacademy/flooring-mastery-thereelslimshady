package org.example.dao;

import org.example.model.Product;
import org.example.model.Tax;

import java.util.List;
import java.util.Map;

public interface ProductDao {

    /**
     * Load all the Product in memory
     */
    void load() throws ProductDataPersistanceException;

    /**
     * Retrieves all the Product List from memory
     */
    List<Product> getAllProducts() throws ProductDataPersistanceException;

    /**
     * Retrieves all the Product map from . Map of product type and product
     */
    Map<String, Product> getProductMap();
}
