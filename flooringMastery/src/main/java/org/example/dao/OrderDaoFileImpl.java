package org.example.dao;

import org.example.model.Order;
import org.example.service.OrderInformationInvalidException;
import org.springframework.stereotype.Component;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class OrderDaoFileImpl implements OrderDao{
    private final String DELIMITER = "," ;
    private String FOLDER_PATH = "Orders/Orders_";

    public OrderDaoFileImpl(String path){
        this.FOLDER_PATH = path;
    }
    public OrderDaoFileImpl(){}

    @Override
    public Order addOrder(Order order, LocalDate date) throws OrderDataPersistanceException {

        Map<Integer, Order> orderMap = new HashMap<>();
        String fileName = String.format(FOLDER_PATH+ date.format(DateTimeFormatter.ofPattern("MMddyyyy")) + ".txt");

        if (fileExists(fileName)) {
            orderMap = load(date);
        }
        orderMap.put(order.getOrderNumber(), order);
        save(date, orderMap);
        return order;
    }

    @Override
    public List<Order> getAllOrderByDay(LocalDate orderDate) throws OrderDataPersistanceException {
        return new ArrayList<>(load(orderDate).values());
    }

    @Override
    public Order removeOrder(int orderNumber, LocalDate orderDate) throws OrderDataPersistanceException {
        Map<Integer, Order> orderMap = new HashMap<>();
        String fileName = String.format(FOLDER_PATH+ orderDate.format(DateTimeFormatter.ofPattern("MMddyyyy")) + ".txt");
        orderMap = load(orderDate);
        Order removed = orderMap.remove(orderNumber);
        save(orderDate, orderMap);
        return removed;
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
    public void save(LocalDate orderDate, Map<Integer, Order> orderMap) throws OrderDataPersistanceException {

        PrintWriter out;
        String fileName = String.format(FOLDER_PATH+ orderDate.format(DateTimeFormatter.ofPattern("MMddyyyy")) + ".txt");

        try {
            File file = new File(fileName);
            // Create the file if it does not exist
            if (!fileExists(fileName) && !orderMap.isEmpty()) {
                if (file.createNewFile()) {
                    System.out.println("File created successfully: " + fileName); //change
                } else {
                    throw new OrderDataPersistanceException("Could not create the order file");
                }
            }
            if (orderMap.isEmpty()){
               file.delete();
               return;
            }

            out = new PrintWriter(new FileWriter(fileName));
        }catch (IOException e){
            throw new OrderDataPersistanceException("Could not save order data.", e);
        }

        String orderAsText;
        out.println("OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total");
        for (Order currentOrder: orderMap.values()){
            orderAsText = marshallOrder(currentOrder);
            out.println(orderAsText);
            out.flush();
        }
        out.close();

    }

     public String marshallOrder(Order order){

        String orderAsText = order.getOrderNumber() + DELIMITER;
        orderAsText += order.getCustomerName() + DELIMITER;
        orderAsText += order.getState() + DELIMITER;
        orderAsText += order.getTaxRate() + DELIMITER;
        orderAsText += order.getProductType() + DELIMITER;
        orderAsText += order.getArea() + DELIMITER;
        orderAsText += order.getCostPerSquareFoot() + DELIMITER;
        orderAsText += order.getLaborCostPerSquareFoot() + DELIMITER;
        orderAsText += order.getMaterialCost() + DELIMITER;
        orderAsText += order.getLaborCost() + DELIMITER;
        orderAsText += order.getTax() + DELIMITER;
        orderAsText += order.getTotal() ;
        return orderAsText;
    }

    @Override
    public Map<Integer, Order> load(LocalDate orderDate) throws OrderDataPersistanceException {
        Scanner scanner;
        String fileName = String.format(FOLDER_PATH+ orderDate.format(DateTimeFormatter.ofPattern("MMddyyyy")) + ".txt");
        Map<Integer, Order> orderMap = new HashMap<>();
        System.out.println(fileName);

        if (!fileExists(fileName)){
            return Collections.emptyMap();
        }

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
    public boolean fileExists(String fileName) throws OrderDataPersistanceException {
        File file = new File(fileName);
        return file.exists();
    }

    public boolean fileExists(LocalDate orderDate) throws OrderDataPersistanceException {
        String fileName = String.format(FOLDER_PATH+ orderDate.format(DateTimeFormatter.ofPattern("MMddyyyy")) + ".txt");
        File file = new File(fileName);
        return file.exists();
    }
}
