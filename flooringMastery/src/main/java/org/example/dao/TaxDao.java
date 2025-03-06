package org.example.dao;

import org.example.model.Tax;

public interface TaxDao {

    void load() throws TaxDataPersistanceException;
    Tax getByName(String state) throws TaxDataPersistanceException;

    String getNameByAbbr(String abbr) throws TaxDataPersistanceException;
}
