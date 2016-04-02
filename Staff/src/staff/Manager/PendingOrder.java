/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.Manager;

/**
 *
 * @author Hena
 */
public class PendingOrder {
    private int orderID;
    private int cusID;

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getCusID() {
        return cusID;
    }

    public void setCusID(int cusID) {
        this.cusID = cusID;
    }

    public int getTableID() {
        return tableID;
    }

    public void setTableID(int tableID) {
        this.tableID = tableID;
    }

    public String getWaiterID() {
        return waiterID;
    }

    public void setWaiterID(String waiterID) {
        this.waiterID = waiterID;
    }
    private int tableID;
    private String waiterID;

    public PendingOrder(int a,int b,int c,String d) {
        orderID=a;
        cusID=b;
        tableID=c;
        waiterID=d;
        
    }
    
}
