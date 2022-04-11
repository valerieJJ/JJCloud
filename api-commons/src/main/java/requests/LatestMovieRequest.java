package requests;

import java.io.Serializable;

public class LatestMovieRequest implements Serializable {
    private int sum;

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public LatestMovieRequest(int sum) {
        this.sum = sum;
    }
}
