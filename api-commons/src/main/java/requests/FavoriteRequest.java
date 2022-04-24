package requests;

import java.io.Serializable;

public class FavoriteRequest implements Serializable {

    private int uid;
    private int mid;

    public FavoriteRequest() {
    }

    public void setUid(int uid) {
        this.uid = uid;
    }


    public FavoriteRequest(int uid, int mid) {
        this.uid = uid;
        this.mid = mid;
    }

    public int getMid() {
        return mid;
    }

    public int getUid() {
        return uid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

}
