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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
    import java.util.ArrayList;
import java.util.Date;

public class InvoiceHeader {
    private int num;
    private Date date;
    private String name;
    private ArrayList<InvoiceLine> lines;

    public double getTotal() {
        double total = 0;
        for (InvoiceLine line : lines) {
            total += line.getTotal();
        }
        return total;
    }
        public ArrayList<InvoiceLine> getLines() {
        return lines;
    }

    public InvoiceHeader(int num, Date date, String name) {
        this.num = num;
        this.date = date;
        this.name = name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
 public void addNewItem(InvoiceLine item) {
        getItemList().add(item);

    }

    public String getDataAsCSV() {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        return "" + getNum() + "," + df.format(getDate()) + "," + getName();
    }

    public ArrayList<InvoiceLine> getItemList() {
        if (lines== null)
            lines = new ArrayList<>();
        return lines;
    }
    
    @Override
    public String toString() {
        return "InvoiceHeader{" +
                "num=" + num +
                ", date=" + date +
                ", name='" + name + '\'' +
                '}';
    }

   
    
}
