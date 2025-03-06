package org.example.dao;

import org.example.model.Product;
import org.example.model.Tax;

import java.util.Map;

public interface TaxDao {

    void load() throws TaxDataPersistanceException;
    Tax getByName(String state) throws TaxDataPersistanceException;

    String getNameByAbbr(String abbr) throws TaxDataPersistanceException;

    Map<String, Tax> getTaxMap();

}
