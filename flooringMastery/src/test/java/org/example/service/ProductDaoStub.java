package org.example.service;

import org.example.dao.ProductDao;
import org.example.dao.ProductDataPersistanceException;
import org.example.model.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ProductDaoStub implements ProductDao {
    @Override
    public void load() throws ProductDataPersistanceException {

    }

    @Override
    public List<Product> getAllProducts() throws ProductDataPersistanceException {
        List<Product> list = new ArrayList<>();
        list.add(new Product("Carpet",new BigDecimal("2.25"), new BigDecimal("2.10")));
        return list;
    }

    @Override
    public Map<String, Product> getProductMap() {
        return null;
    }
}
