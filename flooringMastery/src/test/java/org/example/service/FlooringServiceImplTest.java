package org.example.service;

import org.example.dao.OrderDataPersistanceException;
import org.example.model.Order;
import org.example.model.Product;
import org.example.model.Tax;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FlooringServiceImplTest {

    ApplicationContext ctx = new ClassPathXmlApplicationContext("ApplicationContext.xml");
    FlooringService service = ctx.getBean("service", FlooringService.class);
    Order order1;
    LocalDate date1;

    @BeforeEach
    void setup(){
        date1 = LocalDate.parse("2025-02-01");
        order1 = new Order(date1, "j", new Tax("QC", "Quebec", new BigDecimal("10")), new Product("Clouds", new BigDecimal("3"), new BigDecimal("1")), new BigDecimal("20"));
    }

    @Test
    void failValidateDateInThePast() {
        LocalDate dateInPast = LocalDate.parse("1000-01-01");
        try{
            service.validateDate(dateInPast);
            fail("Should have thrown an exception here");
        }catch (OrderInformationInvalidException e){
            return;
        }catch (Exception e){
            fail("Wrong exception");
        }
    }

    @Test
    void passValidateDateInTheFuture() {
        LocalDate dateInFuture = LocalDate.parse("2026-01-01");
        try{
            service.validateDate(dateInFuture);
            return;
        }catch (OrderInformationInvalidException e){
            fail("Date is in the future");
        }catch (Exception e){
            fail("Wrong exception");
        }
    }

    @Test
    void failValidateCustomerNameWithWeirdCharacters() {
        String weirdCustomerName = "F##@LFj kd7";
        try {service.validateCustomerName(weirdCustomerName);
            fail("Should have thrown an error");}
        catch (OrderInformationInvalidException e){
            return;
        }
        catch (Exception e){
            fail("Wrong exception");
        }
    }

    @Test
    void failValidateEmptyCustomerName() {
        String weirdCustomerName = "";
        try {service.validateCustomerName(weirdCustomerName);
            fail("Should have thrown an error");}
        catch (OrderInformationInvalidException e){
            return;
        }
        catch (Exception e){
            fail("Wrong exception");
        }
    }

    @Test
    void passValidateCustomerName() {
        String goodCustomerName = "Acme, Inc.";
        try {
            service.validateCustomerName(goodCustomerName);
            return;
        }catch (OrderInformationInvalidException e){
                fail("Should have thrown an error");
        }
        catch (Exception e){
            fail("Wrong exception");
        }
    }


    @Test
    void passValidateArea() {
        BigDecimal goodArea = new BigDecimal("55");
        try {
            service.validateArea(goodArea);
            return;
        } catch (OrderInformationInvalidException e){
            fail("Valid area, should not have throw error");
        }
        catch (Exception e){
            fail("Wrong exception");
        }
    }

    @Test
    void failValidateNegativeArea() {
        BigDecimal badArea = new BigDecimal("-10");
        try {
            service.validateArea(badArea);
            fail("Invalid area, should have throw error");
        } catch (OrderInformationInvalidException e){
            return;
        }
        catch (Exception e){
            fail("Wrong exception");
        }
    }

    @Test
    void passAddOrder() {
        try {
            Order.setId(299);
            Order actual = service.addOrder(order1);
            Assertions.assertNotNull(actual);
            Assertions.assertEquals(order1, actual);
        } catch (OrderInformationInvalidException e){
            fail("not supposed to throw an exception");
        }
        catch (Exception e){
            fail("Wrong exception");
        }
    }

    @Test
    void failGetByState() {

        try {
            service.getByState("Quebec");
            fail("Invalid area, should have throw error");
        } catch (TaxInformationInvalidException e){
            return;
        }
        catch (Exception e){
            fail("Wrong exception");
        }
    }

    @Test
    void passGetByState() {

        try {
            Tax actual = service.getByState("new york");
            Assertions.assertNotNull(actual);
            Assertions.assertEquals(new Tax("NY", "New York", new BigDecimal("3")), actual);
        } catch (TaxInformationInvalidException e){
            fail("Valid state, should not have throw error");
        }
        catch (Exception e){
            fail("Wrong exception");
        }
    }

    @Test
    void passGetAllOrders() {

        try {
            order1.setOrderNumber(300);
            List<Order> list = service.getAllOrders(date1);
            Assertions.assertNotNull(list);
            Assertions.assertEquals(1, list.size());
            Assertions.assertEquals(order1, list.get(0));

        } catch (OrderDataPersistanceException e){
            fail("Valid date, should not have throw error");
        }
        catch (Exception e){
            fail("Wrong exception");
        }
    }

    @Test
    void failGetAllOrdersForFileNotExistant() {
        LocalDate noOrderDate = LocalDate.parse("2029-01-01");
        try {

            List<Order> list = service.getAllOrders(noOrderDate);
            fail("Should have thrown error");
        } catch (OrderInformationInvalidException e){
            return;
        }
        catch (Exception e){
            fail("Wrong exception");
        }
    }

    @Test
    void failGetFileNotExistant() {
        LocalDate noOrderDate = LocalDate.parse("2029-01-01");
        try {
            Order found = service.getOrder(order1.getOrderNumber(), noOrderDate);
            fail("Should have thrown error");
        } catch (OrderInformationInvalidException e){
            return;
        }
        catch (Exception e){
            fail("Wrong exception");
        }
    }

    @Test
    void failRemoveOrdernumberNotExistant() {
        try {
            Order found = service.getOrder(400, date1);
            fail("Should have thrown error");
        } catch (OrderInformationInvalidException e){
            return;
        }
        catch (Exception e){
            fail("Wrong exception");
        }
    }

    @Test
    void passRemoveOrder() {
        try {
            order1.setOrderNumber(300);
            Order found = service.getOrder(order1.getOrderNumber(), date1);
            Order removed = service.removeOrder(found.getOrderNumber(), date1);
            Assertions.assertEquals(order1, removed);

        } catch (OrderInformationInvalidException e){
            fail("Should have thrown error");
        }
        catch (Exception e){
            fail("Wrong exception");
        }
    }

}