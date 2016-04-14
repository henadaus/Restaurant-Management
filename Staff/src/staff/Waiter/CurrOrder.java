/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.Waiter;

import java.math.BigInteger;

/**
 *
 * @author Hena
 */
public class CurrOrder {
    private int tid;
    private BigInteger cid;

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public BigInteger getCid() {
        return cid;
    }

    public void setCid(BigInteger cid) {
        this.cid = cid;
    }
    
    CurrOrder(int t,BigInteger c)
    {
        this.tid=t;
        this.cid=c;
    }
}
