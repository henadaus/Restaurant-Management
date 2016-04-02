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
public class CurrOrder {
    private int tid;
    private int cid;

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }
    
    CurrOrder(int t,int c)
    {
        this.tid=t;
        this.cid=c;
    }
}
