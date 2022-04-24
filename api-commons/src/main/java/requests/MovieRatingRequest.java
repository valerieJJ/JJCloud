package requests;

import java.io.Serializable;

public class MovieRatingRequest implements Serializable {

    private int uid;

    private int mid;

    private Double score;

    public MovieRatingRequest() {
    }

    public MovieRatingRequest(int uid, int mid, Double score) {
        this.uid = uid;
        this.mid = mid;
        this.score = score;
    }

    public MovieRatingRequest( int mid, Double score) {
        this.mid = mid;
        this.score = score;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public Double getScore() {
        return Double.parseDouble(String.format("%.2f",score/2D));
    }

    public void setScore(Double score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "MovieRatingRequest{" +
                "uid=" + uid +
                ", mid=" + mid +
                ", score=" + score +
                '}';
    }
}
