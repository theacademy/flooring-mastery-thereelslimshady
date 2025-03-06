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
    Order addOrder(Order order) throws OrderDataPersistanceException;

    Order editOrder(Order order, LocalDate orderDate) throws OrderDataPersistanceException;

    Order removeOrder(int orderNumber, LocalDate orderDate) throws OrderDataPersistanceException;

    Order getOrder(int orderNumber, LocalDate orderDate) throws OrderDataPersistanceException;

    List<Order> getAllOrders(LocalDate orderDate) throws OrderDataPersistanceException;

    List<Product> getAllProducts() throws ProductDataPersistanceException;

    Tax getByState(String state) throws TaxDataPersistanceException, TaxInformationInvalidException;

    void validateDate(LocalDate orderDate);

    void validateCustomerName(String customerName);

    void validateArea(BigDecimal area);

    String getNameByAbbr(String state) throws TaxDataPersistanceException;

    void calculateOrder(Order found);

    String exportAll() throws OrderDataPersistanceException;

    void getInitialOrderId() throws OrderDataPersistanceException;
}
