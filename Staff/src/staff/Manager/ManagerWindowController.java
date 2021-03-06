/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.Manager;

import java.math.BigInteger;
import staff.Waiter.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import staff.Connections.DBConn;
import staff.cashier.PendingBill;

/**
 * FXML Controller class
 *
 * @author Hena
 */
public class ManagerWindowController implements Initializable {

   
    private String mid;
    private static Connection conn;
    //PROCESSING ORDER TABLE
     @FXML
    private TableView<ProcessedOrder> processingOrderTable;
    @FXML
    private TableColumn<ProcessedOrder,Integer> ProcessingOrderColumn;
    @FXML
    private TableColumn<ProcessedOrder,BigInteger> ProcessingCustomerColumn;
    @FXML
    private TableColumn<ProcessedOrder,Integer>ProcessingTableColumn;
    @FXML
    private TableColumn<ProcessedOrder,Long>remTimeColumn;
    @FXML
    private TableColumn<ProcessedOrder,Long>totalTimeColumn;
    @FXML
    private TableColumn<ProcessedOrder,ProgressBar>progressColumn;
    @FXML
    private TableColumn<ProcessedOrder,String>waiterIDColumn;
    @FXML
    private Button getOrderButton;
    @FXML
    private Button startButton;
    @FXML
    private Button forwardButton;
    @FXML
    private Button cancelButton;
     private List<ProcessedOrder> A;
     Callback<TableColumn, TableCell> cellFactory = (TableColumn p) -> new TableCell();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      
        DBConn c=new DBConn();
        conn=c.geConnection();
        
        //ProcessedOrder
        ProcessingOrderColumn.setCellValueFactory(new PropertyValueFactory<>("orderid"));
        ProcessingCustomerColumn.setCellValueFactory(new PropertyValueFactory<>("cusid"));
        ProcessingTableColumn.setCellValueFactory(new PropertyValueFactory<>("tabid"));
        remTimeColumn.setCellValueFactory(e-> e.getValue().lLongProperty().asObject());
        totalTimeColumn.setCellValueFactory(new PropertyValueFactory<>("ttime"));
        progressColumn.setCellValueFactory(new PropertyValueFactory<>("pb"));
        waiterIDColumn.setCellValueFactory(new PropertyValueFactory<>("wid"));
        A=new LinkedList<>();
        getOrderButton.setDisable(false);
        /*startButton.setDisable(true);
        forwardButton.setDisable(true);
        cancelButton.setDisable(true);*/
        
                
    }    
  
   public void initData(String mid)
    {
        this.mid=mid;
        
    }
@FXML
private void onClickgetPendingOrder(ActionEvent e)
{   
    
        //deleting the current table
        ObservableList<ProcessedOrder>allOrders,aa;
         allOrders=processingOrderTable.getItems();
         aa=processingOrderTable.getItems();
         allOrders.removeAll(aa);
    
         ObservableList<ProcessedOrder> list=FXCollections.observableArrayList();
         try {
             Statement st=conn.createStatement();
             String status="pending";
             String query="select * from order_info where status='"+status+"'";
             ResultSet rs=st.executeQuery(query);
           // int f=0;
             int i=0;
             while(rs.next())
             {
            // f++;
                 ProcessedOrder a=new ProcessedOrder(rs.getInt("order_id"), new BigInteger(Long.toString(rs.getLong("c_id"))),rs.getInt("table_id"),rs.getInt("time")*60,rs.getInt("time")*60,rs.getString("waiter_id"));
                 list.add(a);
                 A.add(a);
               }
             
            // if(f!=0)
               processingOrderTable.getItems().addAll(list);
             
         } catch (SQLException ex) {
             Logger.getLogger(CurrentOrderController.class.getName()).log(Level.SEVERE, null, ex);
         }
}

@FXML
private void onMouseClicked(ActionEvent e) throws SQLException{
    ProcessedOrder p=processingOrderTable.getSelectionModel().getSelectedItem();
    int oid=p.getOrderid();
    Statement st=conn.createStatement();
    String query="select status from order_info where order_id="+oid+"";
    ResultSet rs=st.executeQuery(query);
    rs.next();
    String s=rs.getString("status");
    if(s.equals("pending"))
    {
        startButton.setDisable(false);
        forwardButton.setDisable(true);
        cancelButton.setDisable(false);
    }
    else
        if(s.equals("finished"))
        {
            startButton.setDisable(true);
            cancelButton.setDisable(true);
            forwardButton.setDisable(false);
        }
    else
            if(s.equals("processing"))
            {
            startButton.setDisable(true);
            cancelButton.setDisable(true);
            forwardButton.setDisable(true);
            }
}

@FXML
private void onClickStart(ActionEvent e) throws SQLException
{
   /*A.stream().forEach((A1) -> {
        try {
            A1.startProcessing();
        } catch (SQLException ex) {
            Logger.getLogger(ManagerWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    });*/
   
    ProcessedOrder p=processingOrderTable.getSelectionModel().getSelectedItem();
    p.startProcessing();
    Statement st=conn.createStatement();
    System.out.println("waiter_id:"+p.wid);
    String q="insert into process values('"+p.wid+"','"+mid+"',"+p.orderid+")";
    st.executeUpdate(q);
    
    //Forwarding 
}

@FXML
private void onClickForward(ActionEvent e)
{
    ProcessedOrder p=processingOrderTable.getSelectionModel().getSelectedItem();
    Statement st;
        try {
            st = conn.createStatement();
            String s="finished";
            String query="update order_info set status='"+s+"' where order_id="+p.orderid+"";
            st.executeUpdate(query);
            
        } catch (SQLException ex) {
            Logger.getLogger(ProcessedOrder.class.getName()).log(Level.SEVERE, null, ex);
        }
        //deleting the row
         ObservableList<ProcessedOrder>allOrders;
         allOrders=processingOrderTable.getItems();
         
         allOrders.remove((p));
}

@FXML
private void onClickCancel(ActionEvent e)
{
    ProcessedOrder p=processingOrderTable.getSelectionModel().getSelectedItem();
    Statement st;
        try {
            st = conn.createStatement();
            
            String query="delete  from  order_info  where order_id="+p.orderid+"";
            st.executeUpdate(query);
            query="delete  from  process  where order_id="+p.orderid+"";
            st.executeUpdate(query);
            query="delete  from  book_and_order  where order_id="+p.orderid+"";
            st.executeUpdate(query);
        } catch (SQLException ex) {
            Logger.getLogger(ProcessedOrder.class.getName()).log(Level.SEVERE, null, ex);
        }
        //deleting the row
         ObservableList<ProcessedOrder>allOrders;
         allOrders=processingOrderTable.getItems();
         
         allOrders.remove((p));
}
}

