/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author moh.kamal
 */
public class invLineTable extends AbstractTableModel {
     private ArrayList<InvoiceLine> data;
    private String[] cols = {"Item Name", "Count", "Unit Price", "Total Price"};
    
    public invLineTable(ArrayList<InvoiceLine> data) {
        this.data = data;
    }

    public ArrayList<InvoiceLine> getData() {
        return data;
    }

    public int getRowCount() {
        return data.size();
    }

    
    public String getColumnName(int column) {
        return cols[column];
    }
    public int getColumnCount() {
        return cols.length;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        InvoiceLine invItem = data.get(rowIndex);
        switch (columnIndex){
            case 0:
                return invItem.getName();
            case 1:
                return invItem.getCount();
            case 2:
                return invItem.getPrice();
            case 3:
                return invItem.getTotal();
        }
        return "";

    }
}
