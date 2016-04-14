/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rescustomerservices;


import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.lang.NumberFormatException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.DialogPane;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.scene.control.CheckBox;
/**
 * FXML Controller class
 *
 * @author Ashish
 */
public class MobileFXController implements Initializable {
    @FXML
    private TextField mobTextField;
    @FXML
    private TextField seatTextField;
    @FXML
    private Button checkNextButton;
    @FXML
    private Label infoArea;
     @FXML
    private Button confirmButton;
     @FXML
     private static Alert alert;
     @FXML
     private CheckBox guestCheck;
     
     
     //setup of Cussignupt
     @FXML
     Parent root;
     private static Stage stage;
     Scene scene;
     
     
     private long cusid;
     private int tabid;
     private String cname;
     
     //Count for alert and cussignfx
     private boolean acount=true;
     private boolean ccount=true;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        clearall();
        initalertbox();
        try {
            initcusfx();
            // TODO
        } catch (IOException ex) {
            Logger.getLogger(MobileFXController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }    

    @FXML
    private void onClickcheckNextButton(ActionEvent event)  {
        if(guestCheck.isSelected())
        {
            mobTextField.setText("9999999999");
        }
        
        if(ccount){
            stage.initOwner(confirmButton.getScene().getWindow());ccount=false;}
        if(acount){
           alert.initOwner(confirmButton.getScene().getWindow());acount=false;}
        try{
        if(mobTextField.getText().length()!=10  || String.valueOf(Long.parseLong(mobTextField.getText())).length()!=10 || Integer.parseInt(seatTextField.getText())<=0)
                  throw new NumberFormatException();
        } 
         catch(NumberFormatException e)
       {  //System.out.println("Mob no field Not Correct");  
          alert.setContentText("Mobile No or Seat Requirment not Correct!!");
            if(guestCheck.isSelected())
            mobTextField.setText("");
       
          alert.show();
          //Set Text Back to clear if it is Guest
         
          return;
          
         
       }
        
        
        try{
            
            
        ResultSet rs=MobileMain.getCustomer(Long.parseLong(mobTextField.getText()));
        
       if(rs.next())//Shows customer Exist
        {
            System.out.println("Customer alredy registered");
            cusid=rs.getLong("c_id");
            cname=rs.getString("c_name");
            tabid=TableInfoMain.getTable(Integer.parseInt(seatTextField.getText()));
            System.out.print(cusid+cname+tabid);
            if(tabid==0)
            {  alert.setContentText("We are Sorry All Tables are Full Please Try after some time..");
               alert.showAndWait();
            }
            else{
            String str="Hello "+String.valueOf(cname)+"\nAlloted Table :"+ String.valueOf(tabid)+"\nPlease Confirm Your Table ";
            infoArea.setText(str);
            confirmButton.setDisable(false);
            confirmButton.setVisible(true);
            infoArea.setVisible(true);
            }
            
            
        }
       else
       {    clearall();      
            System.out.println("New Customer found");          
            stage.showAndWait();
            
       }
       
       }
      
        catch(Exception e)
        {
            System.out.println("Error in Connection");
           
        }
              
         if(guestCheck.isSelected())
            mobTextField.setText(""); 
    }
    
    
@FXML
private void onClickconfirmButton(ActionEvent event) throws IOException, SQLException
{
     
        TableInfoMain.setTable(cusid, tabid);
        clearall();        
        alert.setContentText("Thank You !! "+cname+"\nYour Table "+tabid+" Confirmed Please Take Seat.");
        alert.showAndWait();
        
}

@FXML
private void onclickmobTextField(Event e)
{
    clearall();
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

private void initcusfx() throws IOException
{
        root = FXMLLoader.load(getClass().getResource("CusSignupFX.fxml"));
        scene = new Scene(root);
        stage=new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
       // stage.initOwner(confirmButton.getScene().getWindow());
        stage.setScene(scene);
        
        
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


private void clearall()
{       confirmButton.setDisable(true);
        infoArea.setText("Enter Your Information");
        mobTextField.setText(null);
        seatTextField.setText(null);
        infoArea.setVisible(false);
        confirmButton.setVisible(false);
    
}
    
}




class MobileMain
{
    public static ResultSet getCustomer(long mobno) throws SQLException
    { 
        DBConn dbc=new DBConn();
        Connection con=dbc.geConnection();
        Statement st=con.createStatement();
        ResultSet rs= st.executeQuery("select * from customer where c_id="+String.valueOf(mobno));
        //System.out.println("Got result "+ rs.getInt(1));
        return rs;
        
    }
}
