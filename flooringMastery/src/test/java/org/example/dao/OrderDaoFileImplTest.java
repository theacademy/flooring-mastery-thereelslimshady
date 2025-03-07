package org.example.dao;

import org.example.model.Order;
import org.example.model.Product;
import org.example.model.Tax;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class OrderDaoFileImplTest {
    OrderDao testDao;
    Order order1;
    String testFolder;
    LocalDate date1;
    String backupFolder;

    public OrderDaoFileImplTest() {
    }

    @BeforeEach
    public void setUp() throws Exception {
        testFolder = "src/test/resources/TestFolder/";
        backupFolder = "src/test/resources/TestBackup/";

        testDao = new OrderDaoFileImpl(testFolder, backupFolder);
        date1 = LocalDate.parse("2025-02-01");
        order1 = new Order(date1, "j", new Tax("QC", "Quebec", new BigDecimal("10")), new Product("Clouds", new BigDecimal("3"), new BigDecimal("1")), new BigDecimal("20"));
        order1.calculateOrder();
        order1.setOrderNumber(300);

    }

    @Test
    void addSaveLoadOrder() {
        try {
            Order expected = testDao.addOrder(order1, LocalDate.parse("2025-02-01"));
            Assertions.assertEquals(order1, expected);

            String fileName = String.format(testFolder + "Orders_" + date1.format(DateTimeFormatter.ofPattern("MMddyyyy")) + ".txt");
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
            new File(String.format(testFolder + "Orders_"+ date1.format(DateTimeFormatter.ofPattern("MMddyyyy")) + ".txt")).delete();


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

            new File(String.format(testFolder+"Orders_"+ date1.format(DateTimeFormatter.ofPattern("MMddyyyy")) + ".txt")).delete();

        } catch (OrderDataPersistanceException e) {
            fail("Should not have thrown error");
        }
    }

    @Test
    void passUpdateOrder() {
        try {
            order1.setOrderNumber(300);
            order1.calculateOrder();
            testDao.addOrder(order1, date1);

            Order order2 = new Order(date1, "Allo,,", new Tax("DC", "District", new BigDecimal("20")), new Product("forest", new BigDecimal("5"), new BigDecimal("2")), new BigDecimal("50"));
            order2.setOrderNumber(300);
            order2.calculateOrder();

            Order edited = testDao.updateOrder(300, date1, order2);
            Assertions.assertEquals(order2, edited);

            new File(String.format(testFolder+ "Orders_" + date1.format(DateTimeFormatter.ofPattern("MMddyyyy")) + ".txt")).delete();

        } catch (OrderDataPersistanceException e) {
            fail("Should not have thrown error");
        }
    }

    @Test
    void passLoadAllAndExportAll() {
        try {
            LocalDate date2 = LocalDate.parse("2013-06-02");
            order1.setOrderNumber(300);
            order1.calculateOrder();
            testDao.addOrder(order1, date1);

            Order order2 = new Order(date1, "Allo", new Tax("DC", "District", new BigDecimal("20")), new Product("forest", new BigDecimal("5"), new BigDecimal("2")), new BigDecimal("50"));
            order2.setOrderNumber(301);
            order2.calculateOrder();
            testDao.addOrder(order2, date1);

            Order order3 = new Order(date2, "Albert Einstein", new Tax("KY", "Kentucky", new BigDecimal("6.00")), new Product("Carpet", new BigDecimal("2.25"), new BigDecimal("2.10")), new BigDecimal("217.00"));
            order3.setOrderNumber(3);
            order3.calculateOrder();
            testDao.addOrder(order3, date2);

            Map<Integer, Order> ordersTest = testDao.loadAll();
            Assertions.assertTrue(ordersTest.containsValue(order1));
            Assertions.assertTrue(ordersTest.containsValue(order2));
            Assertions.assertTrue(ordersTest.containsValue(order3));

            String backupLocation = testDao.saveAll(ordersTest);
            String expected = String.format(backupFolder + "DataExport.txt");
            Assertions.assertEquals(expected, backupLocation);
            Assertions.assertTrue(new File(expected).exists());

            BufferedReader br = new BufferedReader(new FileReader(expected));
            br.readLine(); br.readLine(); br.readLine();
            String actual = br.readLine();
            Assertions.assertEquals("3,Albert Einstein,KY,6.00,Carpet,217.00,2.25,2.10,488.25,455.70,56.64,1000.59,06-02-2013", actual);

            new File(String.format(testFolder+ "Orders_" + date1.format(DateTimeFormatter.ofPattern("MMddyyyy")) + ".txt")).delete();
            new File(String.format(testFolder+ "Orders_" + date2.format(DateTimeFormatter.ofPattern("MMddyyyy")) + ".txt")).delete();
            new File(String.format(backupFolder+ "DataExport.txt")).delete();

        } catch (OrderDataPersistanceException | IOException e) {
            fail("Should not have thrown error");
        }
    }

    @Test
    void failAddSaveLoadOrder() {
        try {
            order1.setTax(null);
            Order expected = testDao.addOrder(order1, LocalDate.parse("2025-02-01"));

            testDao.load( LocalDate.parse("2025-02-01"));

            testDao.removeOrder(order1.getOrderNumber(), LocalDate.parse("2025-02-01"));

            fail("Should have thrown error");

        } catch (OrderDataPersistanceException e) {
            return;

        }finally {
            new File(String.format(testFolder+ "Orders_" + date1.format(DateTimeFormatter.ofPattern("MMddyyyy")) + ".txt")).delete();

        }
    }

    @Test
    void passAddLoadOrderWithCommaInName() {
        try {
            String nameWithComma = "Acme, inc.";
            order1.setCustomerName(nameWithComma);
            Order expected = testDao.addOrder(order1, LocalDate.parse("2025-02-01"));

            Map<Integer, Order> testOrders =  testDao.load( LocalDate.parse("2025-02-01"));
            Assertions.assertEquals(nameWithComma, testOrders.get(order1.getOrderNumber()).getCustomerName());

            new File(String.format(testFolder+ "Orders_" + date1.format(DateTimeFormatter.ofPattern("MMddyyyy")) + ".txt")).delete();

        } catch (OrderDataPersistanceException e) {
            fail("Should not have thrown error");

        }
    }

}