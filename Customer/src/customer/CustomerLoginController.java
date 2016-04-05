/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer;

import customer.Connections.DBConn;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Hena
 */
public class CustomerLoginController implements Initializable {

    private static Connection conn;
    @FXML private TextField cid;
    @FXML private TextField phone;
    private int tableId;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DBConn c=new DBConn();
        conn=c.geConnection();
        
    }    
    
    @FXML
    private void onClickLogin(ActionEvent e) throws SQLException
    {
        String c=cid.getText();
        String p=phone.getText();
        Statement st=conn.createStatement();
        String query="select * from customer where c_id="+Integer.parseInt(c)+" and c_phone='"+p+"'";
        ResultSet rs=st.executeQuery(query);
        if(rs.next())
        {
            TextInputDialog t=new TextInputDialog();
            t.setTitle("Table Booking");
            t.setHeaderText(null);
            t.setContentText("Enter the number. of seats you want");
            Optional<String> result = t.showAndWait();
            if (result.isPresent()){
                Statement st1=conn.createStatement();
                System.out.println(result.get());
                String q="select t_id from table_info where t_seat>="+Integer.parseInt(result.get())+" and c_id=0";
                ResultSet rs1=st1.executeQuery(q);
                if(rs1.next())
                {
                    tableId=rs.getInt(1);
                    Statement st2=conn.createStatement();
                    query="Update table_info set c_id="+Integer.parseInt(c)+" where t_id='"+tableId+"'";
                    st2.executeUpdate(query);
                    
                    Alert a=new Alert(Alert.AlertType.CONFIRMATION);
                    a.setTitle("Confirmation dialog");
                    a.setHeaderText(null);
                    a.setContentText("You have been alotted table "+tableId);
                    a.showAndWait();
                }
                else
                {
                    Alert alert=new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error dialog");
                    alert.setHeaderText(null);
                    alert.setContentText("Table not available.Sorry for inconvenience!!");
                    alert.showAndWait();
                }
            }
            
           
        }
        else
        {
          Alert alert=new Alert(Alert.AlertType.WARNING);
          alert.setTitle("Warning dialog");
          alert.setHeaderText(null);
          alert.setContentText("You don't have an account..Signup first.");
          alert.showAndWait();  
        }
    }
    
    @FXML
    private void onClickSignup(ActionEvent e) throws IOException
    {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("TableBooking.fxml"));
        Parent rt=loader.load();
        Scene signsc=new Scene(rt);
         Stage stg=new Stage();
         stg.setScene(signsc);
         stg.show();
    }
    
}
