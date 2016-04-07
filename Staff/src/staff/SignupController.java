/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import staff.Connections.DBConn;

/**
 * FXML Controller class
 *
 * @author Hena
 */
public class SignupController implements Initializable {

    @FXML private TextField idTextField;
    @FXML private TextField pwdTextField;
    @FXML private ComboBox<String>staffTypeComboBox;
    private ObservableList<String>staffType=FXCollections.observableArrayList();
    private String selectedType;
     private static Connection conn;
     @FXML private Button submitButton;
    /**
     * Initializes the controller class.
     */
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        staffType.add("1");
        staffType.add("2");
        staffType.add("3");
        DBConn c=new DBConn();
        conn=c.geConnection();
        idTextField.clear();
        //pwdTextField.clear();
        staffTypeComboBox.setItems(staffType);
        staffTypeComboBox.setOnAction((event)->{
        selectedType=staffTypeComboBox.getSelectionModel().getSelectedItem();
        });
    }    
    @FXML
    private void onClickSubmit(ActionEvent event) throws SQLException{
        String id=idTextField.getText();
        String p=pwdTextField.getText();
        int type=Integer.parseInt(selectedType);
        Statement st=conn.createStatement();
        if(type==1){//Waiter
        String query="Insert into staff values ('"+id+"',"+type+",1)";
        st.executeUpdate(query);
        query="insert into waiter values('"+id+"','"+p+"')";
        st.executeUpdate(query);
        }
        else if(type==2)//Manager
        {
        String query="Insert into staff values ('"+id+"',"+type+",1)";
        st.executeUpdate(query);
        query="insert into manager values('"+id+"','"+p+"')";
        st.executeUpdate(query);
        }
        else
        if(type==3)//Cashier
        {
        String query="Insert into staff values ('"+id+"',"+type+",1)";
        st.executeUpdate(query);
        query="insert into cashier values('"+id+"','"+p+"')";
        st.executeUpdate(query);
        }
        Alert alert=new Alert(AlertType.INFORMATION);
        alert.setTitle("Information dialog");
        alert.setHeaderText(null);
        alert.setContentText("Congratulations!!Registered successfully :) ");
        alert.showAndWait();
    }
    
}
