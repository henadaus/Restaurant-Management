/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rescustomerservices;
import rescustomerservices.GmailQuickstart;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 * FXML Controller class
 *
 * @author Ashish
 */
public class CusSignupFXController implements Initializable {
    @FXML
    private TextField mobTextField;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField addressTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private Button signupButton;
    @FXML 
    private Button closeButton;
    
    
    @FXML
    Alert alert;
    
    
    private DBConn dbc;
    Connection conn;
    PreparedStatement pst;
    Optional<ButtonType> result;
    
    private boolean acount=true;
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
        try{
       dbc=new DBConn();
       conn=dbc.geConnection();
       pst=conn.prepareStatement("insert into customer values(?,?,?,?)");
        }
        catch(Exception e)
        {
            System.out.println("Database Connection Error!");
        }
       initalertbox();
        // TODO
    }    

    @FXML
    private void onClickSignupButton(ActionEvent event) throws SQLException {
        
        if(acount){
           alert.initOwner(signupButton.getScene().getWindow());acount=false;}
        
        
        try{
        if(mobTextField.getText().length()!=10 || String.valueOf(Long.parseLong(mobTextField.getText())).length()!=10 || nameTextField.getText().length()<=0 || addressTextField.getText().length()<=0 || !isValidEmail(emailTextField.getText()))
                  throw new Exception();
        } 
         catch(Exception e)
       {  //System.out.println("Mob no field Not Correct");  
          alert.setContentText("Information Provided Is Not Correct or Field is Empty");
          alert.show();
          System.out.println(e);
          return;
         
       }
        
        
        try{
        
        pst.setLong(3, Long.parseLong(mobTextField.getText()));
        pst.setString(1,nameTextField.getText());
        pst.setString(4, addressTextField.getText());
        pst.setString(2, emailTextField.getText());
        
        int res=pst.executeUpdate();
        
        if(res==1)
        {
            alert.setContentText("Thank You for Registering With Us \n"+"Please LoginFor booking your Table");
            GmailQuickstart mails=new GmailQuickstart();
            mails.setCusemail(emailTextField.getText());
            mails.setCusmessage("Thank You " + nameTextField.getText()+" You are Now Registered with us\n Hope You Will enjoy Our Service.\n Thank You \n Centrico Restaurant\n Civil Lines \nAllahabad (INDIA)");
            mails.setCussubject("Registration Centrico Restaurant");
            
            Thread t=new Thread(mails);
            t.start();
           result = alert.showAndWait();
            
            if (result.get() == ButtonType.OK){  clearall();                  
                      Stage stage = (Stage) signupButton.getScene().getWindow();
                      stage.close();
                }
            
        }
        }
        catch(Exception e)
         {
            alert.setContentText(" Wrong info Please Enter Your Information Correctly");
            alert.showAndWait();
            System.out.println(e);
            
         }
        
    }
    
    @FXML
   private void onClickcloseButton(ActionEvent e)
   {
       ((Stage)closeButton.getScene().getWindow()).close();
   }
    private void initalertbox()
{          alert = new Alert(Alert.AlertType.INFORMATION);
           alert.setTitle("Information");
           alert.setHeaderText(null);       
          // alert.initOwner(confirmButton.getScene().getWindow());
           alert.initModality(Modality.APPLICATION_MODAL); 
          
           alert.initStyle(StageStyle.UNDECORATED);
           setalertcss();
}

    
    private void setalertcss()
{
     DialogPane dialogPane = alert.getDialogPane();
          
 dialogPane.setStyle("-fx-background-color: linear-gradient(#ffd65b, #e68400)," +
"linear-gradient(#ffef84, #f2ba44)," +
"linear-gradient(#ffea6a, #efaa22)," +
"linear-gradient(#ffe657 0%, #f8c202 50%, #eea10b 100%)," +
"linear-gradient(from 0% 0% to 15% 50%, rgba(255,255,255,0.9), rgba(255,255,255,0));");
 
//dialogPane.getStyleClass().remove("alert");

dialogPane.lookup(".content.label").setStyle("-fx-font-size: 16px; "
        + "-fx-font-weight: bold; -fx-fill: blue;");

ButtonBar buttonBar = (ButtonBar)alert.getDialogPane().lookup(".button-bar");

buttonBar.getButtons().forEach(b->b.setStyle("-fx-background-color: linear-gradient(#ff5400, #be1d00);"
        +"-fx-background-radius: 30;"
        +"-fx-background-insets: 0;"
        +" -fx-text-fill: white;"
        +"-fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 );"));
}
    
    
    
public static boolean isValidEmail(String email) {
   boolean result = true;
   try {
      InternetAddress emailAddr = new InternetAddress(email);
      emailAddr.validate();
   } catch (AddressException ex) {
      result = false;
   }
   return result;
}

private  void clearall()
{
    mobTextField.clear();
    nameTextField.clear();
    addressTextField.clear();
    emailTextField.clear();
}

    
}
