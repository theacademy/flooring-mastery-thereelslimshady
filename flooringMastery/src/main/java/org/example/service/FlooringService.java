package org.example.service;

import org.example.dao.OrderDataPersistanceException;
import org.example.dao.ProductDataPersistanceException;
import org.example.dao.TaxDataPersistanceException;
import org.example.model.Order;
import org.example.model.Product;
import org.example.model.Tax;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface FlooringService {
    /**
     * Basic CRUD operations for orders
     */
    Order addOrder(Order order) throws OrderDataPersistanceException;

    Order editOrder(Order order, LocalDate orderDate) throws OrderDataPersistanceException;

    Order removeOrder(int orderNumber, LocalDate orderDate) throws OrderDataPersistanceException;

    Order getOrder(int orderNumber, LocalDate orderDate) throws OrderDataPersistanceException;

    List<Order> getAllOrders(LocalDate orderDate) throws OrderDataPersistanceException;

    /**
     * List of Products necessary to create/ edit an order
     */
    List<Product> getAllProducts() throws ProductDataPersistanceException;

    /**
     * Returns a tax necessary to create/ edit an order
     */
    Tax getByState(String state) throws TaxDataPersistanceException, TaxInformationInvalidException;

    /**
     * Validation of user input based on the business logic
     */
    void validateDate(LocalDate orderDate);

    void validateCustomerName(String customerName);

    void validateArea(BigDecimal area);

    /**
     * List the state based on its abbreviation
     */
    String getNameByAbbr(String abbreviation) throws TaxDataPersistanceException;

    /**
     * Calculates the different cost based on certain inputs
     */
    void calculateOrder(Order found);

    /**
     * Export all orders in storage
     */
    String exportAll() throws OrderDataPersistanceException;

    /**
     * Necessary to keep track of the max Order Number
     */
    void getInitialOrderId() throws OrderDataPersistanceException;
}
