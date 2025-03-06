package org.example.view;

import org.example.model.Order;
import org.example.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
public class FlooringView {
    UserIo io;

    @Autowired
    public FlooringView(UserIo io){
        this.io=io;
    }
    public int menuSelection(){
          boolean hasErrors = false;
          int option = 0;
          io.print("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
          io.print("* <<Flooring Program>>");
          io.print("* 1. Display Orders");
          io.print("* 2. Add an Order");
          io.print("* 3. Edit an Order");
          io.print("* 4. Remove an Order");
          io.print("* 5. Export All Data");
          io.print("* 6. Quit");
          io.print("*");
          io.print("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
         do {
             try{
                 option =  io.readInt("Choose an option (1-6)");
                 hasErrors = false ;
             }catch (InvalidUserInputException e){
                 displayErrorMessage(e.getMessage());
                 hasErrors = true;
             }
         }while(hasErrors);
          return option;
    }
    public int askOrderNumber(){
       return io.readInt("Enter Order Number");
    }

    public LocalDate askDate(){
        boolean hasErrors = false;
        LocalDate date = null;
        do {
            try {
                date = io.readDate("Enter order date YYYY-MM-DD");
                hasErrors = false;

            } catch (InvalidUserInputException e ) {
                hasErrors = true;
                displayErrorMessage(e.getMessage());
            }
        } while (hasErrors);

        return date;
    }

    public void displayErrorMessage(String e){

        io.print("\n* " + e + "\n");

    }

    public void displayExit(){
        io.print("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        io.print("* Thank you for using the flooring service");
        io.print("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
    }

    public void displayOrder(Order order){
        io.print(order.toString());
    }

    public void displayOrders(List<Order> list){
        for (Order order: list){
            displayOrder(order);
        }
        hitEnterForMenu();
    }

    public String askCutomerName(){
       return io.readString("Enter Customer Name");
    }

    public String askState(){
        return io.readString("Enter a state");
    }

    public Product displayAllProducts(List<Product> list){

        io.print("* Products *");

        for (int i=0; i<list.size(); i++)   {
            io.print((i+1)+". "+list.get(i));
        }
        int index = io.readInt("Choose Product (1-" + list.size() + ")") - 1;
        try{
            return list.get(index);
        }catch (Exception e){throw new InvalidUserInputException("Choose a product from the list");}
    }

    public char displayOrderConfirmation( Order order){
        io.print("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        io.print("* Order");
        io.print("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        displayOrder(order);
        return io.readString("Confirm this order? Y/N").toUpperCase().charAt(0);
    }

    public BigDecimal askArea(){
        try{
            return new BigDecimal(io.readString("Enter Area"));
        }catch (Exception e){throw new InvalidUserInputException("Invalid number");}

    }

    public String askEditName(String oldName){
        return io.readString("Enter new name (" + oldName + ")" );
    }

    public String askEditState(String oldState) {
        return io.readString("Enter new state (" + oldState + ")" );
    }

    public Product askEditProduct(String oldProductType, List<Product> list) {
        io.print("Enter new Product Type (" + oldProductType + ")" );
        for (int i=0; i<list.size(); i++)   {
            io.print((i+1)+". "+list.get(i));
        }
        String input = io.readString("Choose Product (1-"+ list.size() +")");
        if (input == null || input.trim().isEmpty()){
            return null;
        }
        try{
            return list.get(Integer.parseInt(input)-1);
        }catch (Exception e ){
            throw new InvalidUserInputException("Invalid Choice");
        }

    }

    public BigDecimal askEditArea(BigDecimal oldArea) {
         String input = io.readString("Enter new area (" + oldArea + ")" );
        if (input == null || input.trim().isEmpty()){
            return null;
        }
        try{
            return new BigDecimal(input);
        }catch (Exception e ){
            throw new InvalidUserInputException("Invalid Number");
        }
    }

    public void displayExportCreated(String filename) {
         io.print("Export done under filename " + filename +".");
         hitEnterForMenu();
    }

    public String hitEnterForMenu(){
        return io.readString("Hit enter to go back to menu");
    }

    public void displayOrderBanner(){
        io.print("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        io.print("* Display Orders");
        io.print("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
    }

    public void displayAddBanner(){
        io.print("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        io.print("* Add Order");
        io.print("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
    }

    public void displayOrderCreated(int orderNumber) {
        io.print("* Order " + orderNumber + " successfully created.");
        hitEnterForMenu();
    }

    public void displayEditBanner() {
        io.print("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        io.print("* Edit Order");
        io.print("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");

    }

    public void displayOrderEdited(int orderNumber) {
        io.print("* Order " + orderNumber + " successfully updated.");
        hitEnterForMenu();
    }


    public void displayRemoveBanner() {
        io.print("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        io.print("* Remove Order");
        io.print("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");

    }

    public void displayRemoved(int orderNumber) {
        io.print("* Order " + orderNumber + " successfully removed.");
        hitEnterForMenu();
    }

    public void displayExportAllBanner() {
        io.print("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        io.print("* Export All");
        io.print("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
    }
}
