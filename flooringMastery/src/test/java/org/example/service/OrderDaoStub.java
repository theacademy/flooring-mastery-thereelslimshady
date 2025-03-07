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
        if (orderNumber==300){
            return order1;
        }
        return null;
    }

    @Override
    public Order updateOrder(int orderNumber, LocalDate orderDate, Order order) {
        if (orderNumber==300 && orderDate.equals(date1)){
            return order1;
        }
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
    public String saveAll(Map<Integer, Order> orders) throws OrderDataPersistanceException {
        if (!(orders.get(order1.getOrderNumber()) ==null)){
            return "TestBackup/DataExport.txt";
        }
        return null;
    }

    @Override
    public Map<Integer, Order> loadAll() throws OrderDataPersistanceException {
        order1.setOrderNumber(300);
        order1.calculateOrder();

        LocalDate date2 = LocalDate.parse("2025-02-02");
        Order order2= new Order(date2, "j", new Tax("QC", "Quebec", new BigDecimal("10")), new Product("Clouds", new BigDecimal("3"), new BigDecimal("1")), new BigDecimal("20"));
        order2.setOrderNumber(29);
        order2.calculateOrder();

        Map<Integer, Order> testOrder = new HashMap<>();
        testOrder.put(order1.getOrderNumber(), order1);
        testOrder.put(order2.getOrderNumber(), order2);
        return testOrder;
    }


    @Override
    public boolean fileExists(String fileName) throws OrderDataPersistanceException {
        return false;
    }

    @Override
    public boolean fileExists(LocalDate orderDate) throws OrderDataPersistanceException {
        if (orderDate.equals(date1)){
            return true;
        }
        return false;
    }

}
