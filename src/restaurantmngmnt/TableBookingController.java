/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restaurantmngmnt;
import java.io.IOException;
import restaurantmngmnt.Connections.DBConn;

import java.sql.*;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author Hena
 */
public class TableBookingController implements Initializable {
    
    @FXML private TextField nameTextField;
    @FXML private TextField contactTextField;
    @FXML private TextField seatTextField;
    @FXML private Label tableIdLabel;
    @FXML private Button confirmButton;
    private static Connection conn;
    private String name;
    private String phone;
    private int seat;
   
    private int tableId;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       nameTextField.clear();
       contactTextField.clear();
       seatTextField.clear();
       confirmButton.setDisable(true);
       DBConn c=new DBConn();
       conn=c.geConnection();
       
    }
    
    @FXML 
    private void onClickgetTable(ActionEvent event) throws SQLException {
      name=nameTextField.getText();
      phone=contactTextField.getText();
      seat=Integer.parseInt(seatTextField.getText());
      if(!name.equals("") && !phone.equals("") && seat!=0){
      Statement st=conn.createStatement();
      String query="Select t_id from table_info where t_seat>='"+seat+"' and c_id=0";
      ResultSet rs=st.executeQuery(query);
      if(rs.next())
      {
          tableId=rs.getInt(1);
          tableIdLabel.setText(""+tableId);
          confirmButton.setDisable(false);
      }
      else
          tableIdLabel.setText("Table not available..Sorry for inconvenience!!");
      }
      else
          tableIdLabel.setText("Please enter the fields properly.Don't leave any field blank.");
    }
    
    @FXML
    private void onClickConfirm(ActionEvent event) throws SQLException, IOException
    {
        Statement st=conn.createStatement();
        int counter=0;
        //fetching the c_id of the last customer
        String query="select * from customer";
        ResultSet rs=st.executeQuery(query);
        rs.next();
        
            counter=rs.getInt("c_id");
        counter++;
                
        System.out.println("heya:"+counter);
                
         query="Insert into customer (c_id,c_name,c_phone) values("+counter+",'"+name+"','"+phone+"')";
        st.executeUpdate(query);
        
        query="Update table_info set c_id='"+counter+"' where t_id='"+tableId+"'";
        st.executeUpdate(query);
        counter++;
        
         Parent rt = FXMLLoader.load(getClass().getResource("Menu.fxml"));
         Scene signsc=new Scene(rt);
         Stage stg=new Stage();
         stg.setScene(signsc);
         stg.show();
         
    }
    
       
    
}
