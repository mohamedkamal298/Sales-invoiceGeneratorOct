/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author moh.kamal
 */
public class InvoiceLine {
    private int count;
    private String name;
    private double price;
    private InvoiceHeader inv;

    public InvoiceLine(int count, String name, double price, InvoiceHeader inv) {
        this.count = count;
        this.name = name;
        this.price = price;
        this.inv = inv;
    }

    public double getTotal() {
    return count * price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public InvoiceHeader getInv() {
        return inv;
    }

    public void setInv(InvoiceHeader inv) {
        this.inv = inv;
    }

    @Override
    public String toString() {
        return "InvoiceLine{" +
                "count=" + count +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
     public String getDataAsCSV() {
        return "" + getInv().getNum()+ "," + getName() + "," + getPrice()+ "," + getCount();
    }

   
}
