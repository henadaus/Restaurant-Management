/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff;

import java.io.IOException;
import staff.Connections.DBConn;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import java.sql.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import javafx.stage.Stage;


/**
 * FXML Controller class
 *
 * @author Hena
 */

public class StaffLoginController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML private TextField loginIdTextField;
    @FXML private TextField pwdTextField;
    private static Connection conn;
    private int type;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       DBConn c=new DBConn();
       conn=c.geConnection();
       loginIdTextField.clear();
       pwdTextField.clear();
    }    
    @FXML
    private void onClickLogin(ActionEvent event) throws SQLException{
        String id=loginIdTextField.getText();
        String pwd=pwdTextField.getText();
        Statement st=conn.createStatement();
        if(!id.equals("") && !pwd.equals("")){
        String query="select * from staff where s_id='"+id+"'";
        ResultSet rs=st.executeQuery(query);
        if(rs.next()){
        String b=rs.getString("pwd");
        if(b.equals(pwd))
        {
            System.out.println("Login successful");
            type=rs.getInt("s_type");
        }
        else
        {
          Alert alert=new Alert(Alert.AlertType.ERROR);
          alert.setTitle("Error dialog");
          alert.setHeaderText(null);
          alert.setContentText("Ooops!!You entered wrong password.Please try again.");
          alert.showAndWait();
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
        else
        {
          Alert alert=new Alert(Alert.AlertType.WARNING);
          alert.setTitle("Warning dialog");
          alert.setHeaderText(null);
          alert.setContentText("Enter the fields first!!");
          alert.showAndWait();
        }
    }
    
    @FXML
    private void onClickSignup(ActionEvent event) throws IOException{
         Parent rt = FXMLLoader.load(getClass().getResource("Signup.fxml"));
         Scene signsc=new Scene(rt);
         Stage stg=new Stage();
         stg.setScene(signsc);
         stg.show();
    }
    
}
