package org.example.dao;

import org.example.model.Order;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class OrderDaoFileImpl implements OrderDao{
    private final String DELIMITER = "," ;
    private final String FOLDER_PATH = "Orders/Orders_";

    @Override
    public Order addOrder(Order order, LocalDate date) throws OrderDataPersistanceException {
        Map<Integer, Order> orderMap = load(date);
        orderMap.put(order.getOrderNumber(), order);
        save(date, orderMap);
        return null;
    }

    @Override
    public List<Order> getAllOrderByDay(LocalDate orderDate) throws OrderDataPersistanceException {
        return new ArrayList<>(load(orderDate).values());
    }

    @Override
    public Order removeOrder(int orderNumber, LocalDate orderDate) {
        return null;
    }

    @Override
    public Order updateOrder(int orderNumber, LocalDate orderDate, Order order) {
        return null;
    }

    @Override
    public Order getByOrderAndDate(int orderNumber, LocalDate orderDate) {
        return null;
    }

    @Override
    public void save(LocalDate orderDate, Map<Integer, Order> orderMap) {

    }

    @Override
    public Map<Integer, Order> load(LocalDate orderDate) throws OrderDataPersistanceException {
        Scanner scanner;
        String fileName = String.format(FOLDER_PATH+ orderDate.format(DateTimeFormatter.ofPattern("MMddyyyy")) + ".txt");
        Map<Integer, Order> orderMap = new HashMap<>();
        System.out.println(fileName);

        try{
            scanner = new Scanner(new BufferedReader(new FileReader(fileName)));
        }catch (FileNotFoundException e){
            throw new OrderDataPersistanceException(
                    "No orders for this date", e);
        }
        String currentLine;
        Order currentOrder;
        scanner.nextLine();
        while (scanner.hasNextLine()){
            currentLine = scanner.nextLine();
            currentOrder = unmarshallOrder(currentLine);
            currentOrder.setDate(orderDate);
            orderMap.put(currentOrder.getOrderNumber(), currentOrder);
        }
        scanner.close();
        return orderMap;
    }

    private Order unmarshallOrder(String currentLine) {
        String[] input = currentLine.split(DELIMITER);
        return new Order(
                Integer.parseInt(input[0].trim()),
                input[1].trim(),
                input[2].trim(),
                new BigDecimal(input[3].trim()),
                input[4].trim(),
                new BigDecimal(input[5].trim()),
                new BigDecimal(input[6].trim()),
                new BigDecimal(input[7].trim()),
                new BigDecimal(input[8].trim()),
                new BigDecimal(input[9].trim()),
                new BigDecimal(input[10].trim()),
                new BigDecimal(input[11].trim())
        );
    }


    @Override
    public void exportAll() {

    }
}
