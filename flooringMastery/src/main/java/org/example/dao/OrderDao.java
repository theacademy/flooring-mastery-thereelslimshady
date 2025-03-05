package org.example.dao;

import org.example.model.Order;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface OrderDao {

    Order addOrder(Order order, LocalDate orderDate) throws OrderDataPersistanceException;
    List<Order> getAllOrderByDay (LocalDate orderDate) throws OrderDataPersistanceException;
    Order removeOrder(int orderNumber, LocalDate orderDate);
    Order updateOrder(int orderNumber, LocalDate orderDate, Order order);
    Order getByOrderAndDate (int orderNumber, LocalDate orderDate);
    void save(LocalDate orderDate, Map<Integer, Order> orderMap) throws OrderDataPersistanceException;
    Map<Integer, Order> load(LocalDate orderDate) throws OrderDataPersistanceException;
    void exportAll();
    boolean fileExists(String fileName) throws OrderDataPersistanceException;

    }
