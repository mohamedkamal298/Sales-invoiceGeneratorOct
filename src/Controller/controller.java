/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;
import Model.InvoiceLine;;
import Model.InvoiceHeader;
import Model.invLineTable;
import View.SIGFrame;
import View.invDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * @author moh.kamal
 */
public class controller extends JTable implements ActionListener, ListSelectionListener{

    private SIGFrame frame;
    private DateFormat dF= new SimpleDateFormat ("dd-MM-yyyy");
    
    public controller (SIGFrame frame){
        this.frame= frame;
    }
 
    
    @Override
   public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()){
            case "Create New Invoice":
                createInvoice();
                break;
            case "CreateInvoiceOK":
                CreateInvoiceOK();
                break;
            case "CreateInvoiceCancel":
                CreateInvoiceCancel();
                break;
            case "Delete Invoice":
                deleteInvoice();
                break;
                
            case "Delete Item":
                deleteItem();
                break;
            case "Load file":
                loadFile();
                break;
            case "Save file":
                saveFile();
                break;
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        int selectedRow = frame.getInvoiceItemsTable().getSelectedRow();
        if (selectedRow >= 0) {
            InvoiceHeader selectedHeader;
            selectedHeader = frame.getinvHeaderTable().getData().get(selectedRow);
            //fillLabels(selectedRow);            
            frame.getInvNumLabel().setText("" + selectedHeader.getNum());
            frame.getInvoiceDateTextField().setText(dF.format(selectedHeader.getDate()));
            frame.getCustomerNameTextField().setText("" + selectedHeader.getName());
            frame.getInvTotalLabel().setText("" + selectedHeader.getTotal());
            
            ArrayList<InvoiceLine> invItems = selectedHeader.getItemList();            
            frame.setinvLineTable(new invLineTable(invItems)); 
            frame.getInvoicesTable().setModel(frame.getinvLineTable());  
            frame.getinvLineTable().fireTableDataChanged();
        }      
    }

    private void createInvoice(){
        
        frame.setInvoiceDialog(new invDialog(frame));
        frame.getinvDialog().setVisible(true);
    }
    
    private void CreateInvoiceOK() {
        String customerName = frame.getinvDialog().getCustNameTxt().getText();
        String invDate = frame.getinvDialog().getInvDateTxt().getText();
        frame.getinvDialog().setVisible(false);
        frame.getinvDialog().dispose();
        frame.setInvoiceDialog(null);
        try {
            Date invoiceDate = dF.parse(invDate);
            int invoiceNumber = generateInvoiceNumber();
            InvoiceHeader invoiceHeader = new InvoiceHeader(invoiceNumber, invoiceDate, customerName);
            frame.getHeaderList().add(invoiceHeader);
            frame.getinvHeaderTable().fireTableDataChanged();
            displayAllInvoices();
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(frame, "Wrong date Format, correct format is dd-mm-yyyy ", "Error:"+e.getMessage(), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void CreateInvoiceCancel() {
        frame.getinvDialog().setVisible(false);
        frame.getinvDialog().dispose();
        frame.setInvoiceDialog(null);
    }

    private void deleteInvoice() {
        int selectedRow = frame.getInvoicesTable().getSelectedRow();  
        if (selectedRow >=0){
            frame.getinvHeaderTable().getData().remove(selectedRow);
            frame.getinvHeaderTable().fireTableDataChanged();
            frame.setinvLineTable(new invLineTable(new ArrayList<InvoiceLine>()));
            frame.getInvoiceItemsTable().setModel(frame.getinvLineTable());
            frame.getinvLineTable().fireTableDataChanged();
            frame.getCustomerNameTextField().setText("");
            frame.getInvNumLabel().setText("");
            frame.getInvoiceDateTextField().setText("");
            frame.getInvTotalLabel().setText("");
            displayAllInvoices();

            JOptionPane.showMessageDialog(null, "Invoice is deleted");
        }
        else
            JOptionPane.showMessageDialog(frame, "You need to select the invoice before deletion", "Error", JOptionPane.ERROR_MESSAGE);
    }

   

    private void deleteItem() {
        int selectedRow = frame.getInvoiceItemsTable().getSelectedRow();
        if (selectedRow >= 0) {
            InvoiceLine item = frame.getinvLineTable().getData().get(selectedRow);
            frame.getinvLineTable().getData().remove(selectedRow);            
            frame.getinvHeaderTable().fireTableDataChanged();
            frame.getinvLineTable().fireTableDataChanged();
            frame.getInvTotalLabel().setText("" + item.getInv().getTotal());
            JOptionPane.showMessageDialog(null, "Item is deleted successfully!");
            displayAllInvoices();
        } else {
            JOptionPane.showMessageDialog(frame, "You need to select the item before delete item", "Error", JOptionPane.ERROR_MESSAGE);
        }
     
    }

    private void loadFile() {
        try {
            JFileChooser fc = new JFileChooser();            
            if (fc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                Path headerPath = Paths.get(fc.getSelectedFile().getAbsolutePath());
                List<String> hLines = Files.lines(headerPath).collect(Collectors.toList());
                ArrayList<InvoiceHeader> invHeaderList = new ArrayList();
                for (String line : hLines) {
                    InvoiceHeader header;
                    String[] hParts = line.split(",");
                    int invoiceID = Integer.parseInt(hParts[0]);
                    Date invoiceDate = new Date();
                    invoiceDate = dF.parse(hParts[1]);
                    header = new InvoiceHeader(invoiceID, invoiceDate, hParts[2]);
                    invHeaderList.add(header);

                }

                
                if (fc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                    Path itemPath = Paths.get(fc.getSelectedFile().getAbsolutePath());
                    List<String> itemLines = Files.lines(itemPath).collect(Collectors.toList());
                    for (String line : itemLines) {
                        InvoiceLine item;
                        String[] itemParts = line.split(",");
                        //mapping item items to InvoiceItem object
                        int invoiceID = Integer.parseInt(itemParts[0]);
                        double price = Double.parseDouble(itemParts[2]);
                        int count = Integer.parseInt(itemParts[3]);
                        InvoiceHeader header = searchInvoiceHeaderByID(invHeaderList, invoiceID);
                        

                        item = new InvoiceLine( count,itemParts[1], price ,header);
                        header.getItemList().add(item);
                        
                    }
                    frame.setHeaderList(invHeaderList);
                    displayAllInvoices();
                }

            }
        }  catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(frame, "File Not Found Error\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }catch (ParseException e) {
            JOptionPane.showMessageDialog(frame, "Wrong Date Format Error\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }  catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Number Format Error\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Read Error\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void saveFile() {
        String headers = "";
        String items = "";
        try{
        for (InvoiceHeader header : frame.getHeaderList()) {
            headers += header.getDataAsCSV();
            headers += "\n";
            for (InvoiceLine item : header.getItemList()) {
                items += item.getDataAsCSV();
                items += "\n";
            }
        }
        JOptionPane.showMessageDialog(frame, "Select folder to save Header file", "Attention", JOptionPane.WARNING_MESSAGE);
        JFileChooser fc = new JFileChooser();
        if (fc.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
            File headerFile = fc.getSelectedFile();
            try {
                FileWriter headerWriter = new FileWriter(headerFile);
                headerWriter.write(headers);
                headerWriter.flush();
                headerWriter.close();
                JOptionPane.showMessageDialog(frame, "Select folder to save Item file", "Attention", JOptionPane.WARNING_MESSAGE);
                
                if (fc.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                    File itemsFile = fc.getSelectedFile();
                    FileWriter itemWriter = new FileWriter(itemsFile);
                    itemWriter.write(items);
                    itemWriter.flush();
                    itemWriter.close();
                }
                 JOptionPane.showMessageDialog(frame, "Done, Files are saved successfully!");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }catch (NullPointerException ex) {
            JOptionPane.showMessageDialog(frame, "No Invoices to be saved Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private InvoiceHeader searchInvoiceHeaderByID(ArrayList<InvoiceHeader> invHeaderList, int invoiceID) {
        for (InvoiceHeader currentHeader : invHeaderList){
            if (currentHeader.getNum() == invoiceID)
                return currentHeader;
        }
        return null;
    }

    private int generateInvoiceNumber() {
        int lastNum=0;
        for (InvoiceHeader currentHeader : frame.getHeaderList()){
            if (currentHeader.getNum() > lastNum)
                lastNum = currentHeader.getNum();
        }
        return lastNum+1;
    }

    private void displayAllInvoices() {
        for (InvoiceHeader header :frame.getHeaderList()) {
             System.out.println(header);
         }
    }
    

      
}
