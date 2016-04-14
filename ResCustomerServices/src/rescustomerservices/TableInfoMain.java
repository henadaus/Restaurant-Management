/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rescustomerservices;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.*;

/**
 *
 * @author Ashish
 */
public class TableInfoMain {
    private static Connection conn;
    
    public static int getTable(int capacity) throws SQLException
    {
        DBConn cob=new DBConn();
        conn=cob.geConnection();
        Statement st=conn.createStatement();
        String qr="select t_id from table_info where t_seat>="+capacity+" and c_id=0";
        System.out.println(qr);
        ResultSet rs=st.executeQuery(qr);
        int res=0;
         if(rs.next())
        {  System.out.println("Got Table "+rs.getInt("t_id"));
           res=rs.getInt(1);
        }
        st.close();
        conn.close();
       return res;
               
    }
    
    public static int setTable(long customid,int tabid) throws SQLException
    {
        DBConn cob=new DBConn();
        conn=cob.geConnection();
        Statement st=conn.createStatement();
        System.out.println("update table table_info set c_id="+customid+" where t_id="+tabid);
        int res=st.executeUpdate("update table_info set c_id="+customid+" where t_id="+tabid);
        if(res>0){System.out.print("customer "+customid +"Alloted Table"+tabid);return 1;}
        else
            return 0;
        
    }
    
}
