package org.example.service;

import org.example.dao.OrderDataPersistanceException;
import org.example.dao.ProductDataPersistanceException;
import org.example.dao.TaxDataPersistanceException;
import org.example.model.Order;
import org.example.model.Product;
import org.example.model.Tax;

import java.time.LocalDate;
import java.util.List;

public interface FlooringService {
    Order addOrder(Order order) throws OrderDataPersistanceException;
    Order editOrder(Order order, LocalDate orderDate);
    boolean validateValidOrderInformation (Order order);
    Order removeOrder(int orderNumber, LocalDate orderDate);
    Order getOrder(int orderNumber, LocalDate orderDate);
    List<Order> getAllOrders(LocalDate orderDate) throws OrderDataPersistanceException;
    Tax getByState(String state) throws TaxDataPersistanceException, TaxInformationInvalidException;

    List<Product> getAllProducts() throws ProductDataPersistanceException;

    void validateDate(LocalDate orderDate);

    void validateCustomerName(String customerName);
}
