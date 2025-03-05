package org.example.service;

import org.example.dao.*;
import org.example.model.Order;
import org.example.model.Product;
import org.example.model.Tax;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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
    public Order editOrder(Order order, LocalDate orderDate) {
        return null;
    }

    @Override
    public boolean validateValidOrderInformation(Order order) {
        return false;
    }

    @Override
    public Order removeOrder(int orderNumber, LocalDate orderDate) {
        return null;
    }

    @Override
    public Order getOrder(int orderNumber, LocalDate orderDate) {
        return null;
    }

    @Override
    public List<Order> getAllOrders(LocalDate orderDate) throws OrderDataPersistanceException {
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
}
