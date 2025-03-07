package org.example.dao;

import org.example.model.Order;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface OrderDao {

    /**
     * Adds the given order to be persisted in storage based on the order date.
     */
    Order addOrder(Order order, LocalDate orderDate) throws OrderDataPersistanceException;

    /**
     * Lists all the orders given an order date from the storage.
     */
    List<Order> getAllOrderByDay (LocalDate orderDate) throws OrderDataPersistanceException;

    /**
     * Removes the order from the storage based on the order number and order date.
     */
    Order removeOrder(int orderNumber, LocalDate orderDate) throws OrderDataPersistanceException;

    /**
     * Edit the order based its order date and number (that cannot be changed) and the new order will be persisted in storage.
     */
    Order updateOrder(int orderNumber, LocalDate orderDate, Order order) throws OrderDataPersistanceException;

    /**
     * Saves all the orders for a given day in storage with a map of order number and order
     */
    void save(LocalDate orderDate, Map<Integer, Order> orderMap) throws OrderDataPersistanceException;

    /**
     * Loads all the orders in memory for a given day in a map of order number and order
     */
    Map<Integer, Order> load(LocalDate orderDate) throws OrderDataPersistanceException;

    /**
     * Saves all the orders with the order date in storage. Returns the location of the storage
     */
    String saveAll(Map<Integer, Order> orders) throws OrderDataPersistanceException;

    /**
     * Loads all the orders in storage to memory with a map of order number and order
     */
    Map<Integer, Order> loadAll() throws OrderDataPersistanceException;

    /**
     * Validation to see if a given order date exists. Takes filename as an argument
     */
    boolean fileExists(String fileName) throws OrderDataPersistanceException;

    /**
     * Validation to see if a given order date exists. Takes order date as an argument
     */
    boolean fileExists(LocalDate orderDate) throws OrderDataPersistanceException;

    }
