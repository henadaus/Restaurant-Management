/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.cashier;

import java.math.BigInteger;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import staff.Connections.DBConn;
import staff.Waiter.CurrentOrderController;

/**
 * FXML Controller class
 *
 * @author Hena
 */
public class CashierController implements Initializable {
     @FXML
    private TableView<PendingBill> cashierView;
      @FXML
    private TableColumn<PendingBill,Integer> orderID;
    @FXML
    private TableColumn<PendingBill,String> cusName;
    @FXML
    private TableColumn<PendingBill,Float> bill;
    private String caid;
    private static Connection conn;
    //private ObservableList<PendingBill> list;
     Callback<TableColumn, TableCell> cellFactory = (TableColumn p) -> new TableCell();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        cashierView.setEditable(true);
        orderID.setCellValueFactory(new PropertyValueFactory<>("orderID"));
        cusName.setCellValueFactory(new PropertyValueFactory<>("cusName"));
       bill.setCellValueFactory(new PropertyValueFactory<>("bill"));
        DBConn c=new DBConn();
        conn=c.geConnection();
       // list=FXCollections.observableArrayList();
    }    
    
    public void initData(String caid)
    {
        this.caid=caid;
        
    }
    
    @FXML
   private void onClickPendingBill(ActionEvent e)
   {
       ObservableList<PendingBill> list=FXCollections.observableArrayList();
         try {
             Statement st=conn.createStatement();
             int billstatus=0;
             String status="finished";
              
             String query="select * from order_info where bill_paid="+billstatus+" and status='"+status+"'";
             ResultSet rs=st.executeQuery(query);
           // int f=0;
             while(rs.next())
             {
            // f++;
                 System.out.println("in cashierCntroller");
                 BigInteger id=new BigInteger(Long.toString(rs.getLong("c_id")));
                 System.out.println("cid:"+id);
                 Statement st1=conn.createStatement();
                  String query1="select * from customer where c_id="+id+"";
                  ResultSet rs1=st1.executeQuery(query1);
                  rs1.next();
                  System.out.println("Name:"+rs1.getString("c_name"));
                 PendingBill a=new PendingBill(rs.getInt("order_id"), rs1.getString("c_name"),rs.getFloat("cost"));
                 list.add(a);
             }
             
            // if(f!=0)
                cashierView.getItems().addAll(list);
             
             
         } catch (SQLException ex) {
             Logger.getLogger(CurrentOrderController.class.getName()).log(Level.SEVERE, null, ex);
         }
   }
   @FXML
   private void onClickGetPayment(ActionEvent e) throws SQLException
   {
       PendingBill currBill = cashierView.getSelectionModel().getSelectedItem();
        System.out.println(currBill.getCusName()+" "+currBill.getOrderID()+" "+currBill.getBill());
        
        int bill_status=1;
        Statement st=conn.createStatement();
        Statement st1=conn.createStatement();
        Statement st2=conn.createStatement();
        // set bill status 1 in order_info table.
         String query="update order_info set bill_paid='"+bill_status+"' where order_id='"+currBill.getOrderID()+"'";
         st.executeUpdate(query);
         // set cus_id=0 in corrosponding table.
         String query1="select * from order_info where order_id='"+currBill.getOrderID()+"'";
         ResultSet rs=st1.executeQuery(query1);
             rs.next();
             BigInteger cus_id=new BigInteger(Long.toString(rs.getLong("c_id")));
             BigInteger new_cid=BigInteger.ZERO;
             String query2="update table_info set c_id='"+new_cid+"',order_taken=0 where c_id='"+cus_id+"'";
         st2.executeUpdate(query2);
         
         //deleting the row
         ObservableList<PendingBill>billSelected,allBill;
         allBill=cashierView.getItems();
         
         allBill.remove((currBill));
         
        //Inserting into `bill` schema
         int counter=0;
        //fetching the bill_id of the last order
        query="select * from bill";
        rs=st.executeQuery(query);
        while(rs.next())
        {
        counter=rs.getInt("bill_id");
        
        }
        
        counter++;
        String query3="insert into bill values("+counter+","+currBill.bill+")";
        st.executeUpdate(query3);
        //Updating the bill_generation schema
        String query4="insert into bill_generation values("+currBill.orderID+",'"+caid+"',"+counter+")";
        st.executeUpdate(query4);
         
         //alert
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information dialog");
        alert.setHeaderText(null);
        alert.setContentText(" Payment has Received Successfully");
        alert.showAndWait();
   }
}
