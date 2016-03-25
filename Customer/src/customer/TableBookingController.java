/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer;
import java.io.IOException;
import customer.Connections.DBConn;

import java.sql.*;
import java.net.URL;
import java.sql.Connection;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
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
       
       DBConn c=new DBConn();
       conn=c.geConnection();
       
    }
    
    @FXML 
    private void onClickgetTable(ActionEvent event) throws SQLException, IOException {
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
          
          Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
          alert.setTitle("Confirmation dialog");
          alert.setHeaderText(null);
          alert.setContentText("Do you want to confirm the table?");
          ButtonType buttonTypeBook=new ButtonType("Book");
          ButtonType buttonTypeCancel=new ButtonType("Cancel",ButtonBar.ButtonData.CANCEL_CLOSE);
          alert.getButtonTypes().setAll(buttonTypeBook,buttonTypeCancel);
          Optional<ButtonType>result=alert.showAndWait();
    if(result.get()==buttonTypeBook){
          
        int counter=0;
        //fetching the c_id of the last customer
        query="select * from customer";
        rs=st.executeQuery(query);
        while(rs.next())
        {
        counter=rs.getInt("c_id");
        
        }
        
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
          else
          {
              //Customer chooses CLOSE/CANCEL
              System.out.println("Customer did not book the table..");
          }
      }
      else
          tableIdLabel.setText("Table not available..Sorry for inconvenience!!");
      }
      else
          tableIdLabel.setText("Please enter the fields properly.Don't leave any field blank.");
    }
    
   
    
       
    
}
