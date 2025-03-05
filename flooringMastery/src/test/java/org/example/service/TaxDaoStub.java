package org.example.service;

import org.example.dao.TaxDao;
import org.example.dao.TaxDataPersistanceException;
import org.example.model.Tax;

import java.math.BigDecimal;

public class TaxDaoStub implements TaxDao {
    @Override
    public void load() throws TaxDataPersistanceException {

    }

    @Override
    public Tax getByName(String state) throws TaxDataPersistanceException {
       if (state.equals("Quebec")){
           return null;
       }
       if (state.equalsIgnoreCase("New York")){
           return new Tax("NY", "New York", new BigDecimal("3"));
       }
        return null;
    }
}
