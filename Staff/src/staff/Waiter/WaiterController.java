/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.Waiter;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import staff.Connections.DBConn;

/**
 * FXML Controller class
 *
 * @author Hena
 */
public class WaiterController implements Initializable {

    @FXML  private TableView<Order> table;
    private ObservableList data;
    private static Connection conn;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DBConn c=new DBConn();
       conn=c.geConnection();
        try {
            data=getData();
        } catch (SQLException ex) {
            Logger.getLogger(WaiterController.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Size:"+data.size());
        for (Object o : data) {
            System.out.println("heya "+o.toString());
        }
       table.setItems(data);
       //table.getItems().setAll(data);
       table.setVisible(true);
       //table.setEditable(true);
       table.getSelectionModel().selectedIndexProperty().addListener(new RowSelectChangeListener());
   
              }    
    private ObservableList getData() throws SQLException
    {
        List list=new ArrayList();
        Statement st=conn.createStatement();
        
        String query="select * from order_info where status='pending'";
        ResultSet rs=st.executeQuery(query);
        while(rs.next())
        {
            int a=rs.getInt(1);
            int b=rs.getInt(4);
            String c=rs.getString(6);
            int d=rs.getInt(9);
            list.add(new Order(a,b));
            System.out.println("a:"+a+" b:"+b);
                    
        }
        ObservableList data=FXCollections.observableList(list);
        return data;
    }
    private class RowSelectChangeListener implements ChangeListener{

        @Override
        public void changed(ObservableValue observable, Object oldValue, Object newValue) {
             System.out.println("I am in");
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
           
        }
        
    }
}
