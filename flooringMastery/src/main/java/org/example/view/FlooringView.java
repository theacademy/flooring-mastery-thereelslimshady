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
          io.print(" * 1. Display Orders");
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
        io.print(e);
    }

    public void displayExit(){
        io.print("Thank you for using the flooring service");
    }

    public void displayOrder(Order order){
        io.print(order.toString());
    }

    public void displayOrders(List<Order> list){
        for (Order order: list){
            displayOrder(order);
        }
    }

    public String askCutomerName(){
       return io.readString("Enter Customer Name");
    }

    public String askState(){
        return io.readString("Enter a state");
    }

    public Product displayAllProducts(List<Product> list){
        for (int i=0; i<list.size(); i++)   {
            io.print((i+1)+". "+list.get(i));
        }
        return list.get(io.readInt("Choose Product (1-"+ list.size() +")")-1);
    }

    public char displayOrderConfirmation( Order order){
        displayOrder(order);
        return io.readString("Confirm this order? Y/N").toUpperCase().charAt(0);
    }

    public BigDecimal askArea(){
        return new BigDecimal(io.readString("Enter Area"));
    }
//+ askOrderBanner(): void
//+ editOrderBanner(): void
//+ removeOrderBanner(): void
//+ exportAllDataBanner(): void

//+ displayOrder(Order): void
//+ displayOrders(List<Order>): void

}
