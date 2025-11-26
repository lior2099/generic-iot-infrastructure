/*
 FileName: Pizza.java
 Author: Lior Shalom
 Date: 21/07/24
 reviewer: Maya
*/

package vending_machine;

public class Pizza implements Product {
    private double price;
    private final String name;

    public Pizza(String name, double price) {
        this.price = price;
        this.name = name;
    }


    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String getName() {
        return name;
    }
}
