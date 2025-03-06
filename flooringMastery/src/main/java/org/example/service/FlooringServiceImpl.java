package org.example.service;

import org.example.dao.*;
import org.example.model.Order;
import org.example.model.Product;
import org.example.model.Tax;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FlooringServiceImpl implements FlooringService{
    TaxDao taxDao;
    ProductDao productDao;
    OrderDao orderDao;

    @Autowired
    public FlooringServiceImpl(TaxDao taxDao, ProductDao productDao, OrderDao orderDao) {
        this.taxDao = taxDao;
        this.productDao = productDao;
        this.orderDao = orderDao;
    }

    @Override
    public Order addOrder(Order order) throws OrderDataPersistanceException {
        int orderNumber = Order.getId();
        orderNumber++;
        order.setOrderNumber(orderNumber);
        Order.setId(orderNumber);

        return orderDao.addOrder(order, order.getDate());
    }

    @Override
    public Order editOrder(Order order, LocalDate orderDate) throws OrderDataPersistanceException {
        return orderDao.updateOrder(order.getOrderNumber(), orderDate, order);
    }


    @Override
    public Order removeOrder(int orderNumber, LocalDate orderDate) throws OrderDataPersistanceException {
        return orderDao.removeOrder(orderNumber, orderDate);
    }

    @Override
    public Order getOrder(int orderNumber, LocalDate orderDate) throws OrderDataPersistanceException {
        List<Order> orders = getAllOrders(orderDate);
        Order found = orders.stream()
                .filter(o -> o.getOrderNumber()==orderNumber)
                .findFirst()
                .orElse(null);

        if (found == null){
            throw new OrderInformationInvalidException("Order Number Invalid");
        }

        return found;
    }

    @Override
    public List<Order> getAllOrders(LocalDate orderDate) throws OrderDataPersistanceException {

        if (!orderDao.fileExists(orderDate)){
            throw new OrderInformationInvalidException("No orders for that date");
        }
        return orderDao.getAllOrderByDay(orderDate);
    }

    public Tax getByState(String state) throws TaxDataPersistanceException, TaxInformationInvalidException {
        Tax tax= taxDao.getByName(state);
        if (tax == null){
            throw new TaxInformationInvalidException("Cannot deliver in state " + state);
        }
        return tax;
    }

    public List<Product> getAllProducts() throws ProductDataPersistanceException {
        return productDao.getAllProducts();
    }

    @Override
    public void validateDate(LocalDate orderDate) {
        boolean isDateInFuture = orderDate.isAfter(LocalDate.now());
        // validate if it's the correct logic
        if (!isDateInFuture){
            throw new OrderInformationInvalidException("Order date must be in the future");
        }
    }

    @Override
    public void validateCustomerName(String customerName) {
        //limited to characters [a-z][0-9] as well as periods and comma characters.
        if (customerName==null){
            throw new OrderInformationInvalidException("Name cannot be empty");
        }
        if (!customerName.matches("^[a-zA-Z0-9, .]+$")){
            throw new OrderInformationInvalidException("Name can only contain characters (a-z), numbers, comma and periods");
        }
    }

    @Override
    public void validateArea(BigDecimal area) {
        if (area.compareTo(new BigDecimal("0"))<=0){
            throw new OrderInformationInvalidException("Area cannot be 0 or negative");
        }
        if (area.compareTo(new BigDecimal("100"))>=0){
            throw new OrderInformationInvalidException("Area cannot be bigger than 100");
        }
    }
    @Override
    public String getNameByAbbr(String abbr) throws TaxDataPersistanceException {
        return taxDao.getNameByAbbr(abbr);
    }

    @Override
    public void calculateOrder(Order order) {
        order.calculateOrder();
    }

    @Override
    public String exportAll() throws OrderDataPersistanceException {
        Map<Integer, Order> orderMap = orderDao.loadAll();
        return orderDao.exportAll(orderMap);
    }

    @Override
    public void getInitialOrderId() throws OrderDataPersistanceException {
        Map<Integer, Order> orderMap = orderDao.loadAll();
        int maxOrderId = orderMap.keySet().stream()
                .max(Integer::compareTo)
                .orElse(0);
        Order.setId(maxOrderId);
    }

}
