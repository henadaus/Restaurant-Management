/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer;
import customer.Connections.DBConn;
import org.apache.commons.codec.binary.Hex;
import customer.Connections.DBConn;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;

/**
 * FXML Controller class
 *
 * @author Hena
 */
public class MenuController implements Initializable {
    @FXML private CheckBox menu1=new CheckBox();
    @FXML private CheckBox menu2=new CheckBox();
    @FXML private CheckBox menu3=new CheckBox();
    @FXML private CheckBox menu4=new CheckBox();
    @FXML private CheckBox menu5=new CheckBox();
    private float amount;
    private int t_time;
    private static Connection conn;
    private String[] s;
    private int idx;
    private int c_id;
    private int tableId;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DBConn c=new DBConn();
       conn=c.geConnection();
       amount=0;
       t_time=0;
       s=new String[20];
       idx=0;
    }    
    void initData(int cus_id,int tableId)
    {
        this.c_id=cus_id;
        this.tableId=tableId;
        System.out.println("Customer id:"+c_id+" Table id:"+tableId);
    }
    @FXML 
    private void onClickplaceOrder(ActionEvent event) throws SQLException, IOException{
        Statement st=conn.createStatement();
        String query="";
        
        if(menu1.isSelected())
        {
            System.out.println(menu1.getText());
            query="select * from menu where d_name='"+menu1.getText()+"'";
            ResultSet rs=st.executeQuery(query);
            rs.next();
            t_time+=rs.getInt("time");
            amount+=rs.getFloat("price");
            s[idx++]=menu1.getText();
        }
        
        if(menu2.isSelected())
        {
            System.out.println(menu2.getText());
            query="select * from menu where d_name='"+menu2.getText()+"'";
            ResultSet rs=st.executeQuery(query);
            rs.next();
            t_time+=rs.getInt("time");
            amount+=rs.getFloat("price");
            s[idx++]=menu2.getText();
        }
        
        if(menu3.isSelected())
        {
            System.out.println(menu3.getText());
            query="select * from menu where d_name='"+menu3.getText()+"'";
            ResultSet rs=st.executeQuery(query);
            rs.next();
            t_time+=rs.getInt("time");
            amount+=rs.getFloat("price");
            s[idx++]=menu3.getText();
        }
       
       if(menu4.isSelected())
        {
            System.out.println(menu4.getText());
            query="select * from menu where d_name='"+menu4.getText()+"'";
            ResultSet rs=st.executeQuery(query);
            rs.next();
            t_time+=rs.getInt("time");
            amount+=rs.getFloat("price");
            s[idx++]=menu4.getText();
        }
        
       if(menu5.isSelected())
        {
            System.out.println(menu5.getText());
            query="select * from menu where d_name='"+menu5.getText()+"'";
            ResultSet rs=st.executeQuery(query);
            rs.next();
            t_time+=rs.getInt("time");
            amount+=rs.getFloat("price");
            s[idx++]=menu5.getText();
        }
        System.out.println("Total time:"+t_time);
        System.out.println("Total price:"+amount);
        Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation message");
        alert.setHeaderText(null);
        alert.setContentText("Your order has been placed!!!");
        alert.showAndWait();
        getOrder();
    }
    private void getOrder() throws SQLException, IOException
    {
        Statement st=conn.createStatement();
        System.out.println(Arrays.toString(s));
        //serialization
        ByteArrayOutputStream out=new ByteArrayOutputStream();
        new ObjectOutputStream(out).writeObject(s);
        
        String list=new String(Hex.encodeHex(out.toByteArray()));
        System.out.println("IN BYTE FORM:"+list);
        
        //for unique order_id
         int counter=0;
        //fetching the c_id of the last customer
        String query="select * from order_info";
        ResultSet rs=st.executeQuery(query);
        System.out.println("Counter:"+counter);
        while(rs.next())
        {
        counter=rs.getInt("order_id");
        
        }
        
        counter++;
        System.out.println("Counter:"+counter);
        String status="pending";
       String q="insert into order_info values ("+counter+",'"+list+"',"+c_id+","+tableId+","+t_time+",'"+status+"',"+amount+",0,0)";
       // String qu="insert into order values("+counter+",'"+list+"')";
        st.executeUpdate(q);
            
    }
}
