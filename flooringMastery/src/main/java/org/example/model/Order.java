package org.example.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Objects;

public class Order {
    private static int id = 0;
    private int orderNumber;
    private String customerName;
    private String state;
    private BigDecimal taxRate;
    private String productType;
    private BigDecimal area;
    private BigDecimal costPerSquareFoot;
    private BigDecimal laborCostPerSquareFoot;
    private BigDecimal materialCost;
    private BigDecimal laborCost;
    private BigDecimal tax;
    private BigDecimal total;
    private LocalDate date;

    public Order(int orderNumber, String customerName, String state, BigDecimal taxRate, String productType, BigDecimal area, BigDecimal costPerSquareFoot, BigDecimal laborCostPerSquareFoot, BigDecimal materialCost, BigDecimal laborCost, BigDecimal tax, BigDecimal total) {
        this.orderNumber = orderNumber;
        this.customerName = customerName;
        this.state = state;
        this.taxRate = taxRate.setScale(0, RoundingMode.HALF_UP);
        this.productType = productType;
        this.area = area;
        this.costPerSquareFoot = costPerSquareFoot;
        this.laborCostPerSquareFoot = laborCostPerSquareFoot;
        this.materialCost = materialCost;
        this.laborCost = laborCost;
        this.tax = tax;
        this.total = total;
    }

    public Order() {}

    public Order(LocalDate orderDate, String customerName, Tax tax, Product product, BigDecimal area) {
        this.date = orderDate;
        this.customerName = customerName;
        this.state = tax.getStateAbbreviation();
        this.taxRate =  (tax.getTaxRate()).setScale(0, RoundingMode.HALF_UP);
        this.productType = product.getProductType();
        this.costPerSquareFoot= product.getCostPerSquareFoot();
        this.laborCostPerSquareFoot = product.getLaborCostPerSquareFoot();
        this.area = area;
    }

    public void calculateOrder(){

        this.materialCost = multiply(this.getCostPerSquareFoot(), area).setScale(2, RoundingMode.HALF_UP);
        this.laborCost = multiply(this.laborCostPerSquareFoot, area).setScale(2, RoundingMode.HALF_UP);
        this.tax = multiply(add(materialCost,laborCost), taxRate.divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP)).setScale(2, RoundingMode.HALF_UP);
        this.total = add(materialCost, laborCost, this.tax).setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal multiply(BigDecimal first, BigDecimal second){
        return first.multiply(second);
    }

    public static BigDecimal add(BigDecimal first, BigDecimal second){
        return first.add(second);
    }

    public static BigDecimal add(BigDecimal first, BigDecimal second, BigDecimal third){
        return (first.add(second)).add(third);
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public BigDecimal getCostPerSquareFoot() {
        return costPerSquareFoot;
    }

    public void setCostPerSquareFoot(BigDecimal costPerSquareFoot) {
        this.costPerSquareFoot = costPerSquareFoot;
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public BigDecimal getLaborCostPerSquareFoot() {
        return laborCostPerSquareFoot;
    }

    public void setLaborCostPerSquareFoot(BigDecimal laborCostPerSquareFoot) {
        this.laborCostPerSquareFoot = laborCostPerSquareFoot;
    }

    public BigDecimal getMaterialCost() {
        return materialCost;
    }

    public BigDecimal getLaborCost() {
        return laborCost;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return  "Order Number: " + orderNumber +
                "\nCustomer Name: " + customerName  +
                "\nState: " + state  +
                "\nTax Rate: " + taxRate +
                "\nProduct Type: " + productType  +
                "\nArea: " + area +
                "\nCost Per Square Foot: " + costPerSquareFoot +
                "\nLabor Cost Per Square Foot: " + laborCostPerSquareFoot +
                "\nMaterial Cost: " + materialCost +
                "\nLabor Cost: " + laborCost +
                "\nTax: " + tax +
                "\n*** Total: " + total +
                "***\n";
    }

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        Order.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return orderNumber == order.orderNumber && Objects.equals(customerName, order.customerName) && Objects.equals(state, order.state) && Objects.equals(taxRate, order.taxRate) && Objects.equals(productType, order.productType) && Objects.equals(area, order.area) && Objects.equals(costPerSquareFoot, order.costPerSquareFoot) && Objects.equals(laborCostPerSquareFoot, order.laborCostPerSquareFoot) && Objects.equals(materialCost, order.materialCost) && Objects.equals(laborCost, order.laborCost) && Objects.equals(tax, order.tax) && Objects.equals(total, order.total) && Objects.equals(date, order.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderNumber, customerName, state, taxRate, productType, area, costPerSquareFoot, laborCostPerSquareFoot, materialCost, laborCost, tax, total, date);
    }
}
