package vjj.usermodule.model;

import java.io.Serializable;

//@Data
//@AllArgsConstructor
//@NoArgsConstructor
public class TB1 implements Serializable {
    private int mid;
    private String mname;

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
}
