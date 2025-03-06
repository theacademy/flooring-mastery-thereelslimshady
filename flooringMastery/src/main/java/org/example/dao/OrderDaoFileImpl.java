package org.example.dao;

import org.example.model.Order;
import org.springframework.stereotype.Component;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class OrderDaoFileImpl implements OrderDao{
    private final String DELIMITER = "," ;
    private String ORDER_FOLDER = "Orders/";
    private String BACKUP_FOLDER = "Backup2/";
    private String FOLDER_PATH;
    private String BACKUP_PATH;

    public OrderDaoFileImpl(String path){
        this.ORDER_FOLDER = path;
        this.FOLDER_PATH = String.format(ORDER_FOLDER+"Orders_");
    }

    public OrderDaoFileImpl(String path, String backUpPath){
        this.ORDER_FOLDER = path;
        this.FOLDER_PATH = String.format(ORDER_FOLDER+"Orders_");
        this.BACKUP_FOLDER = backUpPath;
        this.BACKUP_PATH = String.format(BACKUP_FOLDER+"DataExport.txt");
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
    public Order updateOrder(int orderNumber, LocalDate orderDate, Order order) throws OrderDataPersistanceException {
        Map<Integer, Order> orderMap = new HashMap<>();
        orderMap = load(orderDate);
        orderMap.replace(orderNumber, order);
        save(orderDate, orderMap);
        return order;
    }

    @Override
    public void save(LocalDate orderDate, Map<Integer, Order> orderMap) throws OrderDataPersistanceException {

        PrintWriter out;
        String fileName = String.format(FOLDER_PATH+ orderDate.format(DateTimeFormatter.ofPattern("MMddyyyy")) + ".txt");

        try {
            File file = new File(fileName);
            // Create the file if it does not exist
            if (!fileExists(fileName) && !orderMap.isEmpty()) {
                file.createNewFile();

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

     public String marshallOrder(Order order) throws OrderDataPersistanceException {
        try {
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
            orderAsText += order.getTotal();
            return orderAsText;
        }catch (Exception e){
            throw new OrderDataPersistanceException("Cannot save order");
        }
    }

    @Override
    public Map<Integer, Order> load(LocalDate orderDate) throws OrderDataPersistanceException {
        Scanner scanner;
        String fileName = String.format(FOLDER_PATH+ orderDate.format(DateTimeFormatter.ofPattern("MMddyyyy")) + ".txt");
        Map<Integer, Order> orderMap = new HashMap<>();

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

    private Order unmarshallOrder(String currentLine) throws OrderDataPersistanceException {
        String[] input = currentLine.split(DELIMITER);
        try {
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
        }catch (Exception e){
            throw new OrderDataPersistanceException("Could not read order");
        }
    }


    @Override
    public Map<Integer, Order> loadAll() throws OrderDataPersistanceException {
        Map<Integer, Order> orders = new HashMap<>();
        File folder = new File(ORDER_FOLDER);

        if (folder.exists()) {
            File[] files = folder.listFiles();

            if (files != null) {
                for (File file : files) {
                    String extractedDate = (file.getName().split("_")[1]).split("\\.txt")[0];
                    LocalDate date = LocalDate.parse(extractedDate, DateTimeFormatter.ofPattern("MMddyyyy"));
                    Map<Integer, Order> ordersByFile = new HashMap<>();
                    ordersByFile = load(date);
                    orders.putAll(ordersByFile);
                }
            } else {
                throw new OrderDataPersistanceException("The folder is empty.");
            }
        } else {
            throw new OrderDataPersistanceException("Folder does not exist or is not a directory.");
        }

        return orders;
    }

    public String exportAll(Map<Integer, Order> orders) throws OrderDataPersistanceException {

        File folder = new File(BACKUP_FOLDER);
        if (orders.isEmpty()){
            throw new OrderDataPersistanceException("No orders to exports");
        }
        try {
           if (!folder.exists()) {
               folder.mkdir();
           }
           File file = new File(BACKUP_PATH);
           if (!file.exists()&& !orders.isEmpty()) {
               file.createNewFile();
           }

           PrintWriter out = new PrintWriter(new FileWriter(BACKUP_PATH));

            String orderAsText;
            out.println("OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total");
            for (Order currentOrder: orders.values()){
                orderAsText = marshallOrder(currentOrder);
                out.println(orderAsText);
                out.flush();
            }
            out.close();


        }catch (Exception e){
           throw new OrderDataPersistanceException("Could not create the backup file.");
       }

        return BACKUP_PATH;
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
