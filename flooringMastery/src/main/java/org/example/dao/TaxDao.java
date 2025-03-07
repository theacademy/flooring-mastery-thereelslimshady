package org.example.dao;

import org.example.model.Product;
import org.example.model.Tax;

import java.util.Map;

public interface TaxDao {

    /**
     * Load all the Tax in memory
     */
    void load() throws TaxDataPersistanceException;

    /**
     * Returns the Tax given a state
     */
    Tax getByName(String state) throws TaxDataPersistanceException;

    /**
     * Returns the state given its abbreviation
     */
    String getNameByAbbr(String abbr) throws TaxDataPersistanceException;

    /**
     * Get the map of stata and Tax from memory
     */
    Map<String, Tax> getTaxMap();

}
