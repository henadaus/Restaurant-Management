/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.Waiter;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


/**
 *
 * @author Hena
 */
public class Order {
    private IntegerProperty orderId;
    private IntegerProperty tableId;
    //private StringProperty status;
    //private IntegerProperty bill_paid;
    public Order(int a,int b)//,String c,int d)
    {
        this.orderId=new SimpleIntegerProperty(a);
        this.tableId=new SimpleIntegerProperty(b);
       // this.status=new SimpleStringProperty(c);
       // this.bill_paid=new SimpleIntegerProperty(d);
    }

    /**
     *
     */
    @Override
   public String toString()
   {
            return "Order id:"+orderId+" Table id: "+tableId;
                    
    }
 
}
