package org.example.controller;

import org.example.dao.OrderDataPersistanceException;
import org.example.dao.ProductDataPersistanceException;
import org.example.dao.TaxDataPersistanceException;
import org.example.model.Order;
import org.example.model.Product;
import org.example.model.Tax;
import org.example.service.FlooringService;
import org.example.service.OrderInformationInvalidException;
import org.example.service.TaxInformationInvalidException;
import org.example.view.FlooringView;
import org.example.view.InvalidUserInputException;
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
    public void run() throws OrderDataPersistanceException {
        boolean keepGoing = true;
        int selection = 0;
        setup();
        while (keepGoing){
            try{
                selection = getMenuAndSelection();

                switch (selection) {
                    case 1:
                        displayOrders();
                        break;
                    case 2:
                        addOrder();
                        break;
                    case 3:
                        editOrder();
                        break;
                    case 4:
                        removeOrder();
                        break;
                    case 5:
                        exportAll();
                        break;
                    case 6:
                    default:
                        keepGoing = false;
                        break;
                }
            }catch (InvalidUserInputException e){
                view.displayErrorMessage(e.getMessage());
            }
            catch (Exception e){
                view.displayErrorMessage(e.getMessage());
            }
        }
        exit();
    }

    public int getMenuAndSelection(){
        int number = view.menuSelection();
        if (number < 1 || number > 6){
            throw new InvalidUserInputException("Invalid selection. Please choose between 1-6");
        }
        return number;
    }

    public void addOrder(){

        boolean hasErrors = false;
        Order created = null;

        view.displayAddBanner();

        LocalDate orderDate = null;
        do {
            try {
                orderDate = view.askDate();
                service.validateDate(orderDate);
                hasErrors = false;
            }catch (InvalidUserInputException | OrderInformationInvalidException e) {
                view.displayErrorMessage(e.getMessage());
                hasErrors = true;
            }
        }while (hasErrors);

        String customerName = null;
        do {
            try {
                customerName = view.askCutomerName();
                service.validateCustomerName(customerName);
                hasErrors = false;
            } catch (InvalidUserInputException | OrderInformationInvalidException e) {
                view.displayErrorMessage(e.getMessage());
                hasErrors = true;
            }
        }while (hasErrors);

        Tax tax = null;
        do{
            try {
                String state = view.askState();
                tax = service.getByState(state);
                hasErrors = false;
            }catch (InvalidUserInputException | OrderInformationInvalidException | TaxDataPersistanceException | TaxInformationInvalidException e){
                view.displayErrorMessage(e.getMessage());
                hasErrors = true;
            }
        }while (hasErrors);

        Product product = null;
        do{
            try {
                List<Product> products = service.getAllProducts();
                product = view.displayAllProducts(products);
                if (product == null) {
                    throw new InvalidUserInputException("Choose a product from the list");
                }
                hasErrors = false;
            }catch (InvalidUserInputException | OrderInformationInvalidException | ProductDataPersistanceException e){
                view.displayErrorMessage(e.getMessage());
                hasErrors = true;
            }
        }while (hasErrors);

        BigDecimal area = null;

        do{
            try {
                area = view.askArea();
                service.validateArea(area);
                hasErrors = false;
            }
            catch (InvalidUserInputException | OrderInformationInvalidException e){
                view.displayErrorMessage(e.getMessage());
                hasErrors = true;
            }
        }while (hasErrors);

        try{
                Order order = new Order(orderDate, customerName, tax, product, area);
                order.setOrderNumber(Order.getId()+1);
                service.calculateOrder(order);

                if (view.displayOrderConfirmation(order)=='Y') {
                    created = service.addOrder(order);
                    view.displayOrderCreated(created.getOrderNumber());
                }

            }catch (OrderDataPersistanceException |
                    InvalidUserInputException | OrderInformationInvalidException e){
                view.displayErrorMessage(e.getMessage());
            }

    }

    public void displayOrders(){

        view.displayOrderBanner();
        List<Order> list;

            try {
                LocalDate date = view.askDate();
                list = service.getAllOrders(date);
                view.displayOrders(list);
            }catch (OrderInformationInvalidException | InvalidUserInputException | OrderDataPersistanceException e){
                view.displayErrorMessage(e.getMessage());
                view.hitEnterForMenu();

            }
    }

    public void removeOrder(){
        view.displayRemoveBanner();
        LocalDate date = view.askDate();
        int orderNumber = view.askOrderNumber();
        try{
            Order found = service.getOrder(orderNumber, date);
            if (view.displayOrderConfirmation(found)=='Y'){
                Order removed = service.removeOrder(orderNumber, date);
                view.displayRemoved(removed.getOrderNumber());
            }
        }catch ( OrderInformationInvalidException
                | OrderDataPersistanceException e) {
            view.displayErrorMessage(e.getMessage());
            view.hitEnterForMenu();
        }

    }

    public void editOrder() {
        boolean hasErrors = false;
        view.displayEditBanner();

        boolean isRecalculationNeeded = false;
        Order found = null;
        LocalDate date = null;
        do {
            date = view.askDate();
            int orderNumber = view.askOrderNumber();
            try {
                found = service.getOrder(orderNumber, date);
                hasErrors = false;
            } catch (InvalidUserInputException | OrderInformationInvalidException | OrderDataPersistanceException e) {
                view.displayErrorMessage(e.getMessage());
                hasErrors = true;
            }
        }while(hasErrors);

        do{
            try{
                String newName = view.askEditName(found.getCustomerName());
                if (!newName.isEmpty()){
                    service.validateCustomerName(newName);
                    found.setCustomerName(newName);
                }
                hasErrors = false;
            } catch (InvalidUserInputException | OrderInformationInvalidException e) {
                view.displayErrorMessage(e.getMessage());
                hasErrors = true;
            }
        }while(hasErrors);

        do{
            try {
                String oldState = service.getNameByAbbr(found.getState());
                String newState = view.askEditState(oldState);

                if (!newState.isEmpty()) {
                    Tax newTax = service.getByState(newState);
                    found.setState(newTax.getStateAbbreviation());
                    found.setTaxRate(newTax.getTaxRate());
                    isRecalculationNeeded = true;
                }
                hasErrors = false;
            }catch (TaxInformationInvalidException | TaxDataPersistanceException | InvalidUserInputException | OrderInformationInvalidException e){
                view.displayErrorMessage(e.getMessage());
                hasErrors = true;
            }
        }while(hasErrors);

        do{
            try {
                List<Product> products = service.getAllProducts();
                Product newProduct = view.askEditProduct(found.getProductType(), products);
                if (newProduct != null) {
                    found.setProductType(newProduct.getProductType());
                    found.setCostPerSquareFoot(newProduct.getCostPerSquareFoot());
                    found.setLaborCostPerSquareFoot(newProduct.getLaborCostPerSquareFoot());
                    isRecalculationNeeded = true;
                }
                hasErrors = false;
            }catch (ProductDataPersistanceException | OrderInformationInvalidException | InvalidUserInputException e){
                view.displayErrorMessage(e.getMessage());
                hasErrors = true;
             }
        }while(hasErrors);

        do{
            try {
                BigDecimal newArea = view.askEditArea(found.getArea());
                if (newArea!=null){
                    service.validateArea(newArea);
                    found.setArea(newArea);
                    isRecalculationNeeded = true;
                    hasErrors = false;
                }
            }catch (InvalidUserInputException | OrderInformationInvalidException e){
                view.displayErrorMessage(e.getMessage());
                hasErrors = true;
            }
        }while(hasErrors);

        try{
            if (isRecalculationNeeded){
                service.calculateOrder(found);
            }

            if (view.displayOrderConfirmation(found)=='Y') {
                Order edited = service.editOrder(found, date);
                view.displayOrderEdited(edited.getOrderNumber());
            }

        } catch (OrderDataPersistanceException | OrderInformationInvalidException | InvalidUserInputException e) {
            view.displayErrorMessage(e.getMessage());
        }
    }

    public void exportAll() throws OrderDataPersistanceException {
        view.displayExportAllBanner();
        try{
            String filename = service.exportAll();
            view.displayExportCreated(filename);
        }catch (OrderDataPersistanceException e){
            view.displayErrorMessage(e.getMessage());
        }

    }

    public void setup() throws OrderDataPersistanceException {
        service.getInitialOrderId();
    }

    public void exit(){
        view.displayExit();
    }
}
