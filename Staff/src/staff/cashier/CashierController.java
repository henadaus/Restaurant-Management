/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.cashier;
import staff.email.GmailQuickstart;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.math.BigInteger;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.apache.commons.codec.binary.Hex;
import staff.Connections.DBConn;
import staff.Waiter.CurrentOrderController;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.commons.codec.DecoderException;
/**
 * FXML Controller class
 *
 * @author Hena
 */
public class CashierController implements Initializable {
     @FXML
    private TableView<PendingBill> cashierView;
      @FXML
    private TableColumn<PendingBill,Integer> orderID;
    @FXML
    private TableColumn<PendingBill,String> cusName;
    @FXML
    private TableColumn<PendingBill,Float> bill;
    private String caid;
    private static Connection conn;
    //private ObservableList<PendingBill> list;
     Callback<TableColumn, TableCell> cellFactory = (TableColumn p) -> new TableCell();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        cashierView.setEditable(true);
        orderID.setCellValueFactory(new PropertyValueFactory<>("orderID"));
        cusName.setCellValueFactory(new PropertyValueFactory<>("cusName"));
       bill.setCellValueFactory(new PropertyValueFactory<>("bill"));
        DBConn c=new DBConn();
        conn=c.geConnection();
       // list=FXCollections.observableArrayList();
    }    
    
    public void initData(String caid)
    {
        this.caid=caid;
        
    }
    
    @FXML
   private void onClickPendingBill(ActionEvent e)
   {
       ObservableList<PendingBill> list=FXCollections.observableArrayList();
         try {
             Statement st=conn.createStatement();
             int billstatus=0;
             String status="finished";
              
             String query="select * from order_info where bill_paid="+billstatus+" and status='"+status+"'";
             ResultSet rs=st.executeQuery(query);
           // int f=0;
             while(rs.next())
             {
            // f++;
                 System.out.println("in cashierCntroller");
                 BigInteger id=new BigInteger(Long.toString(rs.getLong("c_id")));
                 System.out.println("cid:"+id);
                 Statement st1=conn.createStatement();
                  String query1="select * from customer where c_id="+id+"";
                  ResultSet rs1=st1.executeQuery(query1);
                  rs1.next();
                  System.out.println("Name:"+rs1.getString("c_name"));
                 PendingBill a=new PendingBill(rs.getInt("order_id"), rs1.getString("c_name"),rs.getFloat("cost"));
                 list.add(a);
             }
             
            // if(f!=0)
                cashierView.getItems().addAll(list);
             
             
         } catch (SQLException ex) {
             Logger.getLogger(CurrentOrderController.class.getName()).log(Level.SEVERE, null, ex);
         }
   }
   @FXML
   private void onClickGetPayment(ActionEvent e) throws SQLException, IOException, ClassNotFoundException, DocumentException,DecoderException
   {
       PendingBill currBill = cashierView.getSelectionModel().getSelectedItem();
        System.out.println(currBill.getCusName()+" "+currBill.getOrderID()+" "+currBill.getBill());
        
        int orderid=currBill.getOrderID();
        int bill_status=1;
        Statement st=conn.createStatement();
        Statement st1=conn.createStatement();
        Statement st2=conn.createStatement();
        // set bill status 1 in order_info table.
         String query="update order_info set bill_paid='"+bill_status+"' where order_id='"+currBill.getOrderID()+"'";
         st.executeUpdate(query);
         // set cus_id=0 in corrosponding table.
         String query1="select * from order_info where order_id='"+currBill.getOrderID()+"'";
         ResultSet rs=st1.executeQuery(query1);
             rs.next();
             BigInteger cus_id=new BigInteger(Long.toString(rs.getLong("c_id")));
             BigInteger new_cid=BigInteger.ZERO;
             String query2="update table_info set c_id='"+new_cid+"',order_taken=0 where c_id='"+cus_id+"'";
         st2.executeUpdate(query2);
         
         //deleting the row
         ObservableList<PendingBill>billSelected,allBill;
         allBill=cashierView.getItems();
         
         allBill.remove((currBill));
         
        //Inserting into `bill` schema
         int counter=0;
        //fetching the bill_id of the last order
        query="select * from bill";
        rs=st.executeQuery(query);
        while(rs.next())
        {
        counter=rs.getInt("bill_id");
        
        }
        
        counter++;
        String query3="insert into bill values("+counter+","+currBill.bill+")";
        st.executeUpdate(query3);
        //Updating the bill_generation schema
        String query4="insert into bill_generation values("+currBill.orderID+",'"+caid+"',"+counter+")";
        st.executeUpdate(query4);
         
        
        //GENERATING THE BILL IN PDF FORM
        String query5="select * from order_info where order_id="+orderid+" ";
        ResultSet rs1=st.executeQuery(query5);
        rs1.next();
        String itemList=rs1.getString("list");
        int tis=rs1.getInt("table_id");
        float cc=rs1.getFloat("cost");
        //String itemDetails=rs1.getString("item_qty");
        int coupon=rs1.getInt("coupon_id");
        
        float disc_p=0;
        //FETCHING THE DISCOUNT PERCENT CORRESPONDING TO THAT COUPON
        if(coupon!=0){
        Statement st4=conn.createStatement();
        String q4="select discount_percent from coupon where coupon_id="+coupon;
        ResultSet rs4=st4.executeQuery(q4);
        rs4.next();
         disc_p=rs4.getFloat("discount_percent");
        
        }
        //FETCHING CUSTOMER NAME
        String query6="select * from customer where c_id="+cus_id+"";
        ResultSet rs2=st.executeQuery(query6);
        rs2.next();
        String cusname=rs2.getString("c_name");
        String email=rs2.getString("c_email");
        
        String itemsInfo="";
        /*----------------DESERIALIZATION
        ByteArrayInputStream in = new ByteArrayInputStream(Hex.decodeHex(itemList.toCharArray()));
          String[] aa= (String[]) (new ObjectInputStream(in).readObject());
         //System.out.println(Arrays.toString();
          try{
         for (String aa1 : aa) {
             if(aa1!=null){
                 int i;
              String tmp="";
             for( i=0;i<(aa1.length()-1);i++)
             {
                 
                 tmp+=aa1.charAt(i);
             }
             int qty=Integer.parseInt(""+aa1.charAt(i));
             itemsInfo+="Item name:    "+tmp+"  Qty:"+qty+"\n\n";
             
             }
         }
          }
          catch(Exception ee){}
        -----------------------------------------*/
        
        //EXTRACTING INFORMATION ABOUT ORDERED ITEMS
        String tmp="";
        for(int i=0;i<(itemList.length()-1);i++)
        {
            if(itemList.charAt(i+1)=='-')
            {
                int qty=Integer.parseInt(""+itemList.charAt(i));
                Statement st3=conn.createStatement();
                String q3="Select price from menu where d_name='"+tmp+"'";
                ResultSet rs3=st3.executeQuery(q3);
                rs3.next();
                float p=rs3.getFloat("price");
                itemsInfo+="Item name:"+tmp+"  Qty:"+qty+"  Price:+"+p+"    Total:"+(qty*p)+"\n\n";
               
                tmp="";
                i++;
            }
            else
                tmp+=itemList.charAt(i);
        }
        
        itemsInfo+="Total Cost :"+cc+"\n\n";
        if(coupon!=0)
        {
            float tprice=cc-((disc_p*cc)/100);
            itemsInfo+="You have been given a discount of "+disc_p+"%\n\nFinal Cost     :   Rs."+tprice+"\n\n";
        }
        System.out.println("Item info:"+itemsInfo);
        //WRITING TO PDF
        Document doc=new Document();
        
        PdfWriter writer=PdfWriter.getInstance(doc, new FileOutputStream("Order"+orderid+".pdf"));
        doc.open();
        doc.addTitle("CENTRICO RESTAURANT");
        doc.addCreationDate();
        doc.addSubject("BILL");
        //System.out.println(itemDetails);
        
        doc.add(new Paragraph("                         CENTRICO RESTAURANT\n\nORDER ID     :   "+orderid+"\n\nTABLE ID       :   "+tis+"\n\nCUSTOMER NAME     :   "+cusname+"\n\nEMAIL ID       :   "+email+"\n\n"+itemsInfo+"\n\n"));
        
       
        doc.close();
        writer.close();
        
        //SENDING THE BILL IN MAIL TO THE CUSTOMER
        GmailQuickstart a=new GmailQuickstart();
        a.setCusemail(email);
        a.setCusmessage("Thankyou so much "+cusname+" for coming here.Please visit us again :) \n Please find your bill attached below\n");
        a.setCussubject("Bill for order number "+orderid);
        a.setFileName("Order"+orderid+".pdf");
        Thread t=new Thread(a);
        t.start();
        
        //alert
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information dialog");
        alert.setHeaderText(null);
        alert.setContentText(" Payment has Received Successfully");
        alert.showAndWait();
   }
}
