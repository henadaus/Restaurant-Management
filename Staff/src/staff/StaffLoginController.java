/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff;
import staff.Waiter.*;
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
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import staff.Manager.ManagerWindowController;
import staff.cashier.CashierController;


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
    @FXML private PasswordField pwdTextField;
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
    private void onClickLogin(ActionEvent event) throws SQLException, IOException{
        String id=loginIdTextField.getText();
        String pwd=pwdTextField.getText();
        Statement st=conn.createStatement();
        if(!id.equals("") && !pwd.equals("")){
        String query="select * from staff where s_id='"+id+"'";
        ResultSet rs=st.executeQuery(query);
        if(rs.next()){
        
        type=rs.getInt("s_type");
        if(type==1)  //1.waiter 2.Manager 3. Cashier
        {
         Statement st1=conn.createStatement();
         String q="select * from waiter where s_id='"+id+"'";
         ResultSet rs1=st1.executeQuery(q);
         rs1.next();
         String b=rs1.getString("passwd");
         if(b.equals(pwd)){
         FXMLLoader loader=new FXMLLoader(getClass().getResource("Waiter/CurrentOrder.fxml"));
         Parent rt=loader.load();
         Scene signsc=new Scene(rt);
         Stage stg=new Stage();
         stg.setScene(signsc);
         CurrentOrderController controller=loader.<CurrentOrderController>getController();
         controller.initData(id);
         stg.show();
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
       else if(type==2)//Manager
      {
         Statement st1=conn.createStatement();
         String q="select * from manager where s_id='"+id+"'";
         ResultSet rs1=st1.executeQuery(q);
         rs1.next();
         String b=rs1.getString("passwd");
        if(b.equals(pwd)){
        FXMLLoader loader=new FXMLLoader(getClass().getResource("Manager/ManagerWindow.fxml"));
        Parent rt=loader.load();
        Scene signsc=new Scene(rt);
        Stage stg=new Stage();
        stg.setScene(signsc);
        ManagerWindowController controller=loader.<ManagerWindowController>getController();
        controller.initData(id);
         stg.show();
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
        else if(type==3)//cashier
        {
         Statement st1=conn.createStatement();
         String q="select * from cashier where s_id='"+id+"'";
         ResultSet rs1=st1.executeQuery(q);
         rs1.next();
         String b=rs1.getString("passwd");
        if(b.equals(pwd)){
        FXMLLoader loader=new FXMLLoader(getClass().getResource("cashier/cashier.fxml"));
        Parent rt=loader.load();
        Scene signsc=new Scene(rt);
        Stage stg=new Stage();
        stg.setScene(signsc);
        CashierController controller=loader.<CashierController>getController();
        controller.initData(id);
         stg.show();
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
       // }
                       
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
         Parent rt;
        rt = FXMLLoader.load(getClass().getResource("SignUp.fxml"));
         Scene signsc=new Scene(rt);
         Stage stg=new Stage();
         stg.setScene(signsc);
         stg.show();
    }
    
}
