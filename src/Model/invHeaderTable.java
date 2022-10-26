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
public class invHeaderTable extends AbstractTableModel{
     private ArrayList<InvoiceHeader> data;

   
    private String[] cols = {"InvoiceNo", "InvoiceDate", "CustomerName", "Invoice Total"};

    public invHeaderTable(ArrayList<InvoiceHeader> data) {
        this.data = data;
    }
     public ArrayList<InvoiceHeader> getData() {
        return data;
    }
    
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return cols.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        InvoiceHeader invHeader = data.get(rowIndex);
        switch (columnIndex){
            case 0:
                return invHeader.getNum();
            case 1:
                return invHeader.getDate();
            case 2:
                return invHeader.getName();
            case 3:
                return invHeader.getTotal();
        }
        return "";
    }

    @Override
    public String getColumnName(int column) {
        return cols[column]; //To change body of generated methods, choose Tools | Templates.
    }
}
