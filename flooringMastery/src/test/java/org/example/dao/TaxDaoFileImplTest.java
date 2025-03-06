package org.example.dao;

import org.example.model.Order;
import org.example.model.Product;
import org.example.model.Tax;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TaxDaoFileImplTest {

    private String FILEPATH;
    TaxDao testDao;

    public TaxDaoFileImplTest() {
    }

    @BeforeEach
    public void setUp() throws Exception {
        FILEPATH = "src/test/resources/TestData/Taxes.txt";
        testDao = new TaxDaoFileImpl(FILEPATH);

    }

    @Test
    void load() {
        try{
            testDao.load();
            Assertions.assertNotNull(testDao.getTaxMap());
            Assertions.assertTrue(testDao.getTaxMap().containsKey("KENTUCKY"));

        }catch (TaxDataPersistanceException e){
            fail("Should not have thrown an exception");
        }catch (Exception e ){
            fail("Should not have thrown an exception");
        }
    }

    @Test
    void getByName() {
        try{
            Tax tax = testDao.getByName("KENTUCKY");
            Assertions.assertNotNull(tax);
            Assertions.assertEquals(new Tax("KY","Kentucky",new BigDecimal("6.00")), tax);

            Tax badTax = testDao.getByName("QUEBEC");
            Assertions.assertNull(badTax);

        }catch (TaxDataPersistanceException e){
            fail("Should not have thrown an exception");
        }catch (Exception e ){
            fail("Should not have thrown an exception");
        }
    }

    @Test
    void getNameByAbbr() {

        try{
            String name = testDao.getNameByAbbr("KY");
            Assertions.assertNotNull(name);
            Assertions.assertEquals("Kentucky", name);

            String badName = testDao.getNameByAbbr("QC");
            Assertions.assertNull(badName);

        }catch (TaxDataPersistanceException e){
            fail("Should not have thrown an exception");
        }catch (Exception e ){
            fail("Should not have thrown an exception");
        }
    }

}