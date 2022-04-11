package requests;

import java.io.Serializable;

public class HotMovieRequest implements Serializable {
    private int sum;

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public HotMovieRequest(int sum) {
        this.sum = sum;
    }
}
