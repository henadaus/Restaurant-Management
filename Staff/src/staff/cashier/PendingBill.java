/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.cashier;

/**
 *
 * @author Hena
 */
public class PendingBill {
    public int orderID;
    private String cusName;
    public float bill;
    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }
    
    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

   
    

    public float getBill() {
        return bill;
    }

    public void setBill(float bill) {
        this.bill = bill;
    }
     public PendingBill(int a,String n,float b) {
        orderID=a;
        cusName = n;
        bill=b;
        
    }
}
