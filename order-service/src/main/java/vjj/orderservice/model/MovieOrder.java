package vjj.orderservice.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class MovieOrder implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column movie_order.id
     *
     * @mbggenerated Sun Apr 10 22:40:40 CST 2022
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column movie_order.uid
     *
     * @mbggenerated Sun Apr 10 22:40:40 CST 2022
     */
    private Long uid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column movie_order.mid
     *
     * @mbggenerated Sun Apr 10 22:40:40 CST 2022
     */
    private Long mid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column movie_order.price
     *
     * @mbggenerated Sun Apr 10 22:40:40 CST 2022
     */
    private BigDecimal price;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column movie_order.payment_amount
     *
     * @mbggenerated Sun Apr 10 22:40:40 CST 2022
     */
    private BigDecimal paymentAmount;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column movie_order.payment__type
     *
     * @mbggenerated Sun Apr 10 22:40:40 CST 2022
     */
    private Integer paymentType;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column movie_order.pay_time
     *
     * @mbggenerated Sun Apr 10 22:40:40 CST 2022
     */
    private Date payTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table movie_order
     *
     * @mbggenerated Sun Apr 10 22:40:40 CST 2022
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column movie_order.id
     *
     * @return the value of movie_order.id
     *
     * @mbggenerated Sun Apr 10 22:40:40 CST 2022
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column movie_order.id
     *
     * @param id the value for movie_order.id
     *
     * @mbggenerated Sun Apr 10 22:40:40 CST 2022
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column movie_order.uid
     *
     * @return the value of movie_order.uid
     *
     * @mbggenerated Sun Apr 10 22:40:40 CST 2022
     */
    public Long getUid() {
        return uid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column movie_order.uid
     *
     * @param uid the value for movie_order.uid
     *
     * @mbggenerated Sun Apr 10 22:40:40 CST 2022
     */
    public void setUid(Long uid) {
        this.uid = uid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column movie_order.mid
     *
     * @return the value of movie_order.mid
     *
     * @mbggenerated Sun Apr 10 22:40:40 CST 2022
     */
    public Long getMid() {
        return mid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column movie_order.mid
     *
     * @param mid the value for movie_order.mid
     *
     * @mbggenerated Sun Apr 10 22:40:40 CST 2022
     */
    public void setMid(Long mid) {
        this.mid = mid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column movie_order.price
     *
     * @return the value of movie_order.price
     *
     * @mbggenerated Sun Apr 10 22:40:40 CST 2022
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column movie_order.price
     *
     * @param price the value for movie_order.price
     *
     * @mbggenerated Sun Apr 10 22:40:40 CST 2022
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column movie_order.payment_amount
     *
     * @return the value of movie_order.payment_amount
     *
     * @mbggenerated Sun Apr 10 22:40:40 CST 2022
     */
    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column movie_order.payment_amount
     *
     * @param paymentAmount the value for movie_order.payment_amount
     *
     * @mbggenerated Sun Apr 10 22:40:40 CST 2022
     */
    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column movie_order.payment__type
     *
     * @return the value of movie_order.payment__type
     *
     * @mbggenerated Sun Apr 10 22:40:40 CST 2022
     */
    public Integer getPaymentType() {
        return paymentType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column movie_order.payment__type
     *
     * @param paymentType the value for movie_order.payment__type
     *
     * @mbggenerated Sun Apr 10 22:40:40 CST 2022
     */
    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column movie_order.pay_time
     *
     * @return the value of movie_order.pay_time
     *
     * @mbggenerated Sun Apr 10 22:40:40 CST 2022
     */
    public Date getPayTime() {
        return payTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column movie_order.pay_time
     *
     * @param payTime the value for movie_order.pay_time
     *
     * @mbggenerated Sun Apr 10 22:40:40 CST 2022
     */
    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table movie_order
     *
     * @mbggenerated Sun Apr 10 22:40:40 CST 2022
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", uid=").append(uid);
        sb.append(", mid=").append(mid);
        sb.append(", price=").append(price);
        sb.append(", paymentAmount=").append(paymentAmount);
        sb.append(", paymentType=").append(paymentType);
        sb.append(", payTime=").append(payTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}