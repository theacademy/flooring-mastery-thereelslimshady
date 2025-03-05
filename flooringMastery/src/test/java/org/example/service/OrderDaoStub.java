package org.example.service;

import org.example.dao.OrderDao;
import org.example.dao.OrderDataPersistanceException;
import org.example.model.Order;
import org.example.model.Product;
import org.example.model.Tax;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public class OrderDaoStub implements OrderDao {
    LocalDate date1 = LocalDate.parse("2025-02-01");
    Order order1= new Order(date1, "j", new Tax("QC", "Quebec", new BigDecimal("10")), new Product("Clouds", new BigDecimal("3"), new BigDecimal("1")), new BigDecimal("20"));

    @Override
    public Order addOrder(Order order, LocalDate orderDate) throws OrderDataPersistanceException {
        if (order.getOrderNumber() == 300 && orderDate.equals(date1)) {
            return order;
        }else return null;
    }

    @Override
    public List<Order> getAllOrderByDay(LocalDate orderDate) throws OrderDataPersistanceException {
        if (orderDate.equals(date1)){
            order1.setOrderNumber(300);
            return new ArrayList<>(Arrays.asList(order1));
        }
        return null;
    }

    @Override
    public Order removeOrder(int orderNumber, LocalDate orderDate) {
        return null;
    }

    @Override
    public Order updateOrder(int orderNumber, LocalDate orderDate, Order order) {
        return null;
    }

    @Override
    public Order getByOrderAndDate(int orderNumber, LocalDate orderDate) {
        return null;
    }

    @Override
    public void save(LocalDate orderDate, Map<Integer, Order> orderMap) {

    }

    @Override
    public Map<Integer, Order> load(LocalDate orderDate) throws OrderDataPersistanceException {
        return null;
    }

    @Override
    public void exportAll() {

    }

    @Override
    public boolean fileExists(String fileName) throws OrderDataPersistanceException {
        return false;
    }

}
