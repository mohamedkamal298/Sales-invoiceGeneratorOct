/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 *
 * @author moh.kamal
 */
public class invDialog extends JDialog{
     private JLabel customerNameLbl;
    private JTextField customerNameTxt;    
    private JLabel dateLabel;
    private JTextField invoiceDateTxt;
    private JButton okButton;
    private JButton cancelButton;
    public invDialog(SIGFrame frame) {

        customerNameLbl = new JLabel("Customer Name:");
        customerNameTxt = new JTextField(20);
        dateLabel = new JLabel("Invoice Date:");
        invoiceDateTxt = new JTextField(20);
        
        okButton = new JButton("OK");
       okButton.setActionCommand("CreateInvoiceOK");        
        okButton.addActionListener(frame.getcontroller());
        
        cancelButton = new JButton("Cancel"); 
        cancelButton.setActionCommand("CreateInvoiceCancel");
        cancelButton.addActionListener(frame.getcontroller());
        
        setLayout(new GridLayout(3, 2));
        
        add(dateLabel);
        add(invoiceDateTxt);
        add(customerNameLbl);
        add(customerNameTxt);
        add(okButton);
        add(cancelButton);
        pack();        
    }

    public JTextField getCustNameTxt() {
        return customerNameTxt;
    }

    public JTextField getInvDateTxt() {
        return invoiceDateTxt;
    }
}
