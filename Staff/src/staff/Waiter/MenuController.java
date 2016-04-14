/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.Waiter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.commons.codec.binary.Hex;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import staff.Connections.DBConn;

/**
 * FXML Controller class
 *
 * @author Hena
 */
public class MenuController implements Initializable {

    private BigInteger c_id;
    private int tableId;
    private static Connection conn;
    private String[] s;
    private int idx;
    private String wid;
    @FXML private Label currentCustomer;
    /**
     * Initializes the controller class.
     */
    @FXML
    private TextField couponTextField;
    @FXML
    private TableView<Menu> menutable;
    @FXML
    private TableColumn<Menu,Integer> mid;
    @FXML
    private TableColumn<Menu,String> mname;
    @FXML
    private TableColumn<Menu,Float> mprice;
    @FXML
    private TableColumn<Menu,String> mquantity;
    @FXML 
    private Label totalPrice;
    @FXML
    private Button getMenuButton;
    @FXML
    private Button getListButton;
     Callback<TableColumn, TableCell> cellFactory = (TableColumn p) -> new TableCell();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DBConn c=new DBConn();
       conn=c.geConnection();
       
       
        menutable.setEditable(true);
        mid.setCellValueFactory(new PropertyValueFactory<>("mid"));
        mname.setCellValueFactory(new PropertyValueFactory<>("mname"));
        mprice.setCellValueFactory(new PropertyValueFactory<>("mprice"));
        mquantity.setCellValueFactory(new PropertyValueFactory<>("mquantity"));
        mquantity.setEditable(true);

        mquantity.setCellFactory(TextFieldTableCell.forTableColumn());
        mquantity.setOnEditCommit((TableColumn.CellEditEvent<Menu,String> t) -> {
            //System.out.println("Hey hena :* ");
            ((Menu) t.getTableView().getItems().get(
                    t.getTablePosition().getRow())
                    ).setMquantity(t.getNewValue());
        });
       s=new String[20];
       idx=0;
       getListButton.setDisable(true);
       couponTextField.setEditable(false);
    }    
    void initData(int tableId,BigInteger cus_id,String wid)
    {
        this.c_id=cus_id;
        this.tableId=tableId;
        this.wid=wid;
        System.out.println("Customer id:"+c_id+" Table id:"+tableId);
        currentCustomer.setText(""+tableId);
    }
    @FXML
    private void onClickgetMenuButton(ActionEvent e)
    {
        ObservableList<Menu> list=FXCollections.observableArrayList();
         try {
             Statement st=conn.createStatement();
             String query="select * from menu";
             ResultSet rs=st.executeQuery(query);
             int f=0;
             while(rs.next())
             {
                 f++;
                 Menu a=new Menu(rs.getInt("d_id"), rs.getString("d_name"),rs.getFloat("price"),"0");
                 list.add(a);
             }
             
             if(f!=0)
               menutable.getItems().addAll(list);
             
         } catch (SQLException ex) {
             Logger.getLogger(CurrentOrderController.class.getName()).log(Level.SEVERE, null, ex);
         }
         getListButton.setDisable(false);
         couponTextField.setEditable(true);
    }
    @FXML
    private void onClickgetListButton(ActionEvent e) throws SQLException, IOException
    {
        getMenuButton.setDisable(true);
       
        Statement st=conn.createStatement();
        ObservableList<Menu> totallist=menutable.getItems();
        ObservableList<Menu> orderlist=FXCollections.observableArrayList();
        float tprice=0;
        int ttime=0;
        
        for(Menu i:totallist)
        {
            if(Integer.parseInt(i.getMquantity())>0)
            {
             orderlist.add(i);
             System.out.println("Id:"+i.getMid()+" Item : "+i.getMname()+" Price "+i.getMprice()+" Quantity: "+i.getMquantity());
             tprice+=(i.getMprice() * Integer.parseInt(i.getMquantity()));
             
             
             String query="select ttime from menu where d_id='"+i.getMid()+"'";
             ResultSet rs=st.executeQuery(query);
             rs.next();
             ttime+=( rs.getInt("ttime")* Integer.parseInt(i.getMquantity()));
                System.out.println("Total time:"+ttime);
                
             s[idx++]=i.getMname();
            }
        }
        
         //Checking the validity of coupon if it exists
        if(couponTextField.getText()!=""){
            
        int coupon=Integer.parseInt(couponTextField.getText());
        
        String q="select * from coupon where coupon_id='"+coupon+"' ";
        ResultSet rs1=st.executeQuery(q);
            if(rs1.next())
            {
            Alert a=new Alert(Alert.AlertType.CONFIRMATION);
            a.setTitle("Confirmation");
            a.setHeaderText(null);
            a.setContentText("Great!!Customer has a valid coupon");
            a.showAndWait();
            float discount=rs1.getFloat("discount_percent");
            tprice-=(tprice*discount)/100;
            }
            else
            {
            Alert a=new Alert(Alert.AlertType.ERROR);
            a.setTitle("Error");
            a.setHeaderText(null);
            a.setContentText("The coupon is not a valid one!!!!");
            a.showAndWait();
            }
        
        
        }
        
        
        System.out.println("total price:"+tprice);
        totalPrice.setText("Total bill"+tprice);
        //Making an entry in `order` table
        //serialization
        ByteArrayOutputStream out=new ByteArrayOutputStream();
        new ObjectOutputStream(out).writeObject(s);
        
        String list=new String(Hex.encodeHex(out.toByteArray()));
        System.out.println("IN BYTE FORM:"+list);
        
        //for unique order_id
         int counter=0;
        //fetching the c_id of the last customer
        String query="select * from order_info";
        ResultSet rs=st.executeQuery(query);
        System.out.println("Counter:"+counter);
        while(rs.next())
        {
        counter=rs.getInt("order_id");
        
        }
        
        counter++;
        System.out.println("Counter:"+counter);
        String status="pending";
        //PLACING ORDER
        String q="insert into order_info values("+counter+",'"+list+"',"+tableId+","+ttime+",'"+status+"',"+tprice+",'"+wid+"',0,"+c_id+")";
        st.executeUpdate(q);
        //UPDATING TABLE_INFO
        q="update table_info set order_taken=1 where t_id="+tableId+" ";
        st.executeUpdate(q);
        //UPDATING `book_and_order
        q="insert into book_and_order values("+tableId+","+counter+","+c_id+")";
        st.executeUpdate(q);
    }
}
