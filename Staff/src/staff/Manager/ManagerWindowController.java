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
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

     @FXML
    private TableView<PendingOrder> pendingOrderTable;
    @FXML
    private TableColumn<PendingOrder,Integer> orderID;
    @FXML
    private TableColumn<PendingOrder,Integer> cusID;
    @FXML
    private TableColumn<PendingOrder,Integer> tableID;
    @FXML
    private TableColumn<PendingOrder,String> waiterID;
    private static Connection conn;
     Callback<TableColumn, TableCell> cellFactory = (TableColumn p) -> new TableCell();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       pendingOrderTable.setEditable(true);
        tableID.setCellValueFactory(new PropertyValueFactory<>("tableID"));
        cusID.setCellValueFactory(new PropertyValueFactory<>("cusID"));
        orderID.setCellValueFactory(new PropertyValueFactory<>("orderID"));
        waiterID.setCellValueFactory(new PropertyValueFactory<>("waiterID"));
        DBConn c=new DBConn();
        conn=c.geConnection();
    }    
   @FXML
   private void onClickgetOrder(ActionEvent e)
   {
        ObservableList<PendingOrder> list=FXCollections.observableArrayList();
         try {
             Statement st=conn.createStatement();
             String status="pending";
             String query="select * from order_info where status='"+status+"'";
             ResultSet rs=st.executeQuery(query);
           // int f=0;
             while(rs.next())
             {
            // f++;
                 PendingOrder a=new PendingOrder(rs.getInt(1), rs.getInt(3),rs.getInt(4),rs.getString(8));
                 list.add(a);
             }
             
            // if(f!=0)
                pendingOrderTable.getItems().addAll(list);
             
         } catch (SQLException ex) {
             Logger.getLogger(CurrentOrderController.class.getName()).log(Level.SEVERE, null, ex);
         }
   }
}
