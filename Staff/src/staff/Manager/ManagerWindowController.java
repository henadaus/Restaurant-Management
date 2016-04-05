/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.Manager;

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
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import staff.Connections.DBConn;

/**
 * FXML Controller class
 *
 * @author Hena
 */
public class ManagerWindowController implements Initializable {

   
    
    private static Connection conn;
    //PROCESSING ORDER TABLE
     @FXML
    private TableView<ProcessedOrder> processingOrderTable;
    @FXML
    private TableColumn<ProcessedOrder,Integer> ProcessingOrderColumn;
    @FXML
    private TableColumn<ProcessedOrder,Integer> ProcessingCustomerColumn;
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
        
    }    
  
   
@FXML
private void onClickgetPendingOrder(ActionEvent e)
{
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
                 ProcessedOrder a=new ProcessedOrder(rs.getInt(1), rs.getInt(3),rs.getInt(4),rs.getInt(5)*60,rs.getInt(5)*60,rs.getString(8));
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
private void onClickForward(ActionEvent e)
{
    A.stream().forEach((A1) -> {
        A1.startProcessing();
    });
}
}

