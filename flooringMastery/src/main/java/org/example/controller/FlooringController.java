package org.example.controller;

import org.example.dao.OrderDataPersistanceException;
import org.example.dao.ProductDataPersistanceException;
import org.example.dao.TaxDataPersistanceException;
import org.example.model.Order;
import org.example.model.Product;
import org.example.model.Tax;
import org.example.service.FlooringService;
import org.example.service.OrderInformationInvalidException;
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
                //update
                case 4: removeOrder(); break;
                //exportAll
                case 6:
                default: keepGoing=false; break;
            }

        }
        exit();
    }

    public int getMenuAndSelection(){
        return view.menuSelection();
    }

    public void addOrder(){

        boolean hasErrors = false;
        Order created = null;
        do {
            try {
                //Right now, if any of the fields have an error, return at the beginning
                LocalDate orderDate = view.askDate();
                service.validateDate(orderDate);

                String customerName = view.askCutomerName();
                service.validateCustomerName(customerName);

                String state = view.askState();
                Tax tax = service.getByState(state);

                List<Product> products = service.getAllProducts();
                Product product = view.displayAllProducts(products);

                BigDecimal area = view.askArea();
                service.validateArea(area);

                Order order = new Order(orderDate, customerName, tax, product, area); //missing order number
                if (view.displayOrderConfirmation(order)=='Y') {
                    created = service.addOrder(order);
                    view.displayOrder(created);
                }
                hasErrors = false;

            }catch (OrderDataPersistanceException | TaxDataPersistanceException | ProductDataPersistanceException |
                    TaxInformationInvalidException |OrderInformationInvalidException e){
                hasErrors = true;
                view.displayErrorMessage(e.getMessage());
            }

        } while (hasErrors);

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

    public void removeOrder(){
        LocalDate date = view.askDate();
        int orderNumber = view.askOrderNumber();
        try{
            Order found = service.getOrder(orderNumber, date);
            if (view.displayOrderConfirmation(found)=='Y'){
                Order removed = service.removeOrder(orderNumber, date);
                view.displayOrder(removed);
            }
        }catch ( OrderInformationInvalidException
                | OrderDataPersistanceException e) {
            view.displayErrorMessage(e.getMessage());

        }

    }

    public void exit(){
        view.displayExit();
    }
}
