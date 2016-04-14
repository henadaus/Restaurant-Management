/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.Waiter;

import java.io.IOException;
import java.math.BigInteger;
import staff.Connections.DBConn;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import staff.cashier.PendingBill;

/**
 * FXML Controller class
 *
 * @author Hena
 */
public class CurrentOrderController implements Initializable {

    private String wid;
     @FXML
    private TableView<CurrOrder> currentOrderTable;
    @FXML
    private TableColumn<CurrOrder,Integer> tableID;
    @FXML
    private TableColumn<CurrOrder,BigInteger> cusID;
    private static Connection conn;
     Callback<TableColumn, TableCell> cellFactory = new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn p) {
                return new TableCell();
            }
        };
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)  {
        currentOrderTable.setEditable(true);
        tableID.setCellValueFactory(new PropertyValueFactory<>("tid"));
        cusID.setCellValueFactory(new PropertyValueFactory<>("cid"));
        DBConn c=new DBConn();
        conn=c.geConnection();
       
        
    }
    
    public void initData(String wid)
    {
        this.wid=wid;
        
    }
    @FXML
    private void onClickgetTableButton(ActionEvent e)
    {
        ObservableList<CurrOrder> list=FXCollections.observableArrayList();
         try {
             Statement st=conn.createStatement();
             String query="select * from table_info where c_id > 0 and order_taken=0";
             ResultSet rs=st.executeQuery(query);
            int f=0;
             while(rs.next())
             {
             f++;
                 CurrOrder a;
                 a = new CurrOrder(rs.getInt("t_id"), new BigInteger(Long.toString(rs.getLong("c_id"))));
                 list.add(a);
             }
             
             if(f!=0)
               currentOrderTable.getItems().addAll(list);
             
         } catch (SQLException ex) {
             Logger.getLogger(CurrentOrderController.class.getName()).log(Level.SEVERE, null, ex);
         }
    }
    
    @FXML
    private void onClickProceedButton(ActionEvent e) throws IOException
    {
        CurrOrder corder = currentOrderTable.getSelectionModel().getSelectedItem();
        System.out.println(corder.getTid()+" "+corder.getCid());
        
        //deleting a row
        ObservableList<CurrOrder>orderSelected,allOrder;
        allOrder=currentOrderTable.getItems();
        allOrder.remove((corder));
         
        FXMLLoader loader=new FXMLLoader(getClass().getResource("Menu.fxml"));
        Parent rt=loader.load();
        Scene signsc=new Scene(rt);
         Stage stg=new Stage();
         stg.setScene(signsc);
         MenuController controller=loader.<MenuController>getController();
         controller.initData(corder.getTid(),corder.getCid(),wid);
         stg.show();
        
    }
    
}
