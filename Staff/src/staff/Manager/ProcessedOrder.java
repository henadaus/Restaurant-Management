/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.Manager;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import staff.Connections.DBConn;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressBar;

/**
 *
 * @author Hena
 */
public class ProcessedOrder implements Runnable{
    public int orderid;
    private int cusid;
    private int tabid;
    private String waiterid;
    private long rtime;
    private long ttime;
    private ProgressBar pb = new ProgressBar(0);
    private LongProperty l;
    public String wid;
    private static Connection conn;
    
    public String getWid() {
        return wid;
    }

    public void setWid(String wid) {
        this.wid = wid;
    }
    public String getWaiterid() {
        return waiterid;
    }

    public void setWaiterid(String waiterid) {
        this.waiterid = waiterid;
    }
    
    
    public int getOrderid() {
        return orderid;
    }

    public void setOrderid(int orderid) {
        this.orderid = orderid;
    }

    public int getCusid() {
        return cusid;
    }

    public void setCusid(int cusid) {
        this.cusid = cusid;
    }

    public int getTabid() {
        return tabid;
    }

    public void setTabid(int tabid) {
        this.tabid = tabid;
    }

    public long getRtime() {
        return rtime;
    }

    public void setRtime(long rtime) {
        this.rtime = rtime;
    }

    public long getTtime() {
        return ttime;
    }

    public void setTtime(long ttime) {
        this.ttime = ttime;
    }

    public ProgressBar getPb() {
        return pb;
    }

    public void setPb(ProgressBar pb) {
        this.pb = pb;
    }

    public LongProperty getL() {
        return l;
    }

    public void setL(LongProperty l) {
        this.l = l;
    }
     public LongProperty lLongProperty()
   {
       return l;
   }

     ProcessedOrder(int orderid, int cusid, int tabid,long rtime, int ttime,String wid) {
        this.orderid = orderid;
        this.cusid = cusid;
        this.tabid = tabid;
        l=new SimpleLongProperty(rtime);
        this.ttime = ttime;
        this.wid=wid;      
        DBConn c=new DBConn();
        conn=c.geConnection();
    }

     public void startProcessing() throws SQLException
     {
        Thread t=new Thread(this);
        t.start();
       
     }
    
    @Override
    public void run() {
        long starttime=System.currentTimeMillis();
        
        while((System.currentTimeMillis()-starttime)<ttime)
        {
            long rtime=System.currentTimeMillis()-starttime;
            long r=ttime - System.currentTimeMillis() +starttime;
            l.set(r);
            double f=(double)rtime/(double)ttime;
            System.out.println("percentage="+f+" Remaining time "+rtime);
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(ProcessedOrder.class.getName()).log(Level.SEVERE, null, ex);
            }
            pb.setProgress(f);
        }
        l.set(0);
        pb.setProgress(1);
        Statement st;
        try {
            st = conn.createStatement();
            String s="finished";
            String query="update order_info set status='"+s+"' where order_id="+orderid+"";
            st.executeUpdate(query);
            /*Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation dialog");
            alert.setHeaderText(null);
            alert.setContentText("Order"+this.orderid+" completed successfully!!");
            alert.showAndWait();*/
        } catch (SQLException ex) {
            Logger.getLogger(ProcessedOrder.class.getName()).log(Level.SEVERE, null, ex);
        }
                      
        
        System.out.println("Order "+orderid+"Completed");
    
    }
}
