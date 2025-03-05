package org.example.service;

import org.example.dao.ProductDao;
import org.example.dao.ProductDataPersistanceException;
import org.example.model.Product;

import java.util.List;

public class ProductDaoStub implements ProductDao {
    @Override
    public void load() throws ProductDataPersistanceException {

    }

    @Override
    public List<Product> getAllProducts() throws ProductDataPersistanceException {
        return null;
    }

    @Override
    public Product getByProductType(String productType) {
        return null;
    }
}
