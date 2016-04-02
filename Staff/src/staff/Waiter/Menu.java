/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.Waiter;

/**
 *
 * @author Hena
 */
public class Menu {
    private int mid;

    private String mname;
    private float mprice;
    private String mquantity;
    
    Menu()
    {
        
    }
    Menu(int mid,String mname,float mprice,String mquantity)
    {
        this.mid=mid;
        this.mname=mname;
        this.mprice=mprice;
        this.mquantity=mquantity;
        
    }
    
    
    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public double getMprice() {
        return mprice;
    }

    public void setMprice(float mprice) {
        this.mprice = mprice;
    }

    public String getMquantity() {
        return mquantity;
    }

    public void setMquantity(String mquantity) {
        this.mquantity = mquantity;
    }
}
