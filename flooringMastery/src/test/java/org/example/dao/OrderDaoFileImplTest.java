package org.example.dao;

import org.example.model.Order;
import org.example.model.Product;
import org.example.model.Tax;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderDaoFileImplTest {
    OrderDao testDao;
    Order order1;
    String testFolder;
    LocalDate date1;

    public OrderDaoFileImplTest() {
    }

    @BeforeEach
    public void setUp() throws Exception {
        testFolder = "src/test/resources/TestFolder/Orders_";

        testDao = new OrderDaoFileImpl(testFolder);
        date1 = LocalDate.parse("2025-02-01");
        order1 = new Order(date1, "j", new Tax("QC", "Quebec", new BigDecimal("10")), new Product("Clouds", new BigDecimal("3"), new BigDecimal("1")), new BigDecimal("20"));
        order1.setOrderNumber(300);

    }

    @Test
    void addSaveLoadOrder() {
        try {
            Order expected = testDao.addOrder(order1, LocalDate.parse("2025-02-01"));
            Assertions.assertEquals(order1, expected);

            String fileName = String.format(testFolder + date1.format(DateTimeFormatter.ofPattern("MMddyyyy")) + ".txt");
            Assertions.assertTrue(testDao.fileExists(fileName));

            Assertions.assertEquals(order1, testDao.load(date1).get(order1.getOrderNumber()));

            new File(fileName).delete();
        } catch (OrderDataPersistanceException e) {
            fail("Should not have thrown error");
        }
    }

    @Test
    void displayAllOrders() {
        try {
            testDao.addOrder(order1, date1);

            List<Order> expected = testDao.getAllOrderByDay(date1);
            Assertions.assertEquals(1, expected.size());
            Assertions.assertEquals(order1, expected.get(0));
            new File(String.format(testFolder+ date1.format(DateTimeFormatter.ofPattern("MMddyyyy")) + ".txt")).delete();


        } catch (OrderDataPersistanceException e) {
            fail("Should not have thrown error");
        }
    }

    @Test
    void remove1Order() {
        try {
            testDao.addOrder(order1, date1);
            Order removed = testDao.removeOrder(300, date1);
            Assertions.assertEquals(order1, removed);
            Assertions.assertFalse(testDao.fileExists(date1));

        } catch (OrderDataPersistanceException e) {
            fail("Should not have thrown error");
        }
    }

    @Test
    void remove2Order() {
        try {
            order1.setOrderNumber(300);
            testDao.addOrder(order1, date1);
            order1.setOrderNumber(301);
            testDao.addOrder(order1, date1);
            Order removed = testDao.removeOrder(300, date1);

            order1.setOrderNumber(300);
            Assertions.assertEquals(order1, removed);
            Assertions.assertTrue(testDao.fileExists(date1));

            List<Order> list =testDao.getAllOrderByDay(date1);
            Assertions.assertEquals(1, list.size());

            new File(String.format(testFolder+ date1.format(DateTimeFormatter.ofPattern("MMddyyyy")) + ".txt")).delete();

        } catch (OrderDataPersistanceException e) {
            fail("Should not have thrown error");
        }
    }
}