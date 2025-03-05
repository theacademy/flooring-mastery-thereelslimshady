package org.example.controller;

import org.example.dao.OrderDataPersistanceException;
import org.example.dao.ProductDataPersistanceException;
import org.example.dao.TaxDataPersistanceException;
import org.example.model.Order;
import org.example.model.Product;
import org.example.model.Tax;
import org.example.service.FlooringService;
import org.example.service.ProductInformationInvalidException;
import org.example.service.TaxInformationInvalidException;
import org.example.view.FlooringView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
public class FlooringController {

    FlooringView view;
    FlooringService service;

    @Autowired
    public FlooringController(FlooringView view, FlooringService service) {
        this.view = view;
        this.service = service;
    }
    public void run(){
        boolean keepGoing = true;
        int selection = 0;
        while (keepGoing){
            selection = getMenuAndSelection();
            switch (selection){
                case 1: displayOrders(); break;
                case 2: addOrder(); break;
                case 6:
                default: keepGoing=false; break;
            }

        }
        exit();
    }

    public int getMenuAndSelection(){
        return view.menuSelection();
    }

    public Order addOrder(){

        boolean hasErrors = false;
        do {
            try {
                LocalDate orderDate = view.askDate(); // how do we validate the date is in the future?
                service.validateDate(orderDate);

                String customerName = view.askCutomerName();
                service.validateCustomerName(customerName);

                String state = view.askState();
                Tax tax = service.getByState(state);

                List<Product> products = service.getAllProducts();
                Product product = view.displayAllProducts(products);

                BigDecimal area = view.askArea(); //validate area

                Order order = new Order(orderDate, customerName, tax, product, area); //missing order number
                if (view.displayOrderConfirmation(order)=='Y') {
                    Order created = service.addOrder(order);
                    view.displayOrder(created);
                }
                hasErrors = false;
            }catch (OrderDataPersistanceException | TaxDataPersistanceException | ProductDataPersistanceException |
                    TaxInformationInvalidException e){
                hasErrors = true;
                view.displayErrorMessage(e.getMessage());
            }

        } while (hasErrors);


        return null;
    }

    public void displayOrders(){
        //for now, display is not looking great because using to string !!
        try {
            LocalDate date = view.askDate();
            List<Order> list = service.getAllOrders(date);
            view.displayOrders(list);
        }catch (OrderDataPersistanceException e){
            view.displayErrorMessage(e.getMessage());
        }

    }

    public void exit(){
        view.displayExit();
    }
}
