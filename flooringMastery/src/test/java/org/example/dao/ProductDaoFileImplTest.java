package org.example.dao;

import org.example.model.Product;
import org.example.model.Tax;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ProductDaoFileImplTest {
    private String FILEPATH;
    ProductDao testDao;

    ProductDaoFileImplTest(){}

    @BeforeEach
    public void setUp() throws Exception {
        FILEPATH = "src/test/resources/TestData/Products.txt";
        testDao = new ProductDaoFileImpl(FILEPATH);
    }


    @Test
    void load() {

        try{
            testDao.load();
            Assertions.assertNotNull(testDao.getProductMap());
            Assertions.assertTrue(testDao.getProductMap().containsKey("CARPET"));

        }catch (ProductDataPersistanceException e){
            fail("Should not have thrown an exception");
        }catch (Exception e ){
            fail("Should not have thrown an exception");
        }
    }


    @Test
    void getAllProducts() {
        try {
            List<Product> list = testDao.getAllProducts();
            Assertions.assertEquals(4, list.size());
            Assertions.assertTrue(list.contains(new Product("Carpet", new BigDecimal("2.25"), new BigDecimal("2.10"))));
        }catch (ProductDataPersistanceException e){
        fail("Should not have thrown an exception");
        }catch (Exception e ){
        fail("Should not have thrown an exception");
        }
    }

}