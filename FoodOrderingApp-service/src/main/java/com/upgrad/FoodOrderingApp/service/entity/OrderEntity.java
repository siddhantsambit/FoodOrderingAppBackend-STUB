package com.upgrad.FoodOrderingApp.service.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name="orders", schema = "restaurantdb")
public class OrderEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Column(name="uuid")
    @NotNull
    @Size(max=200)
    private String uuid;

    @Column(name="bill")
    @NotNull
    private BigDecimal bill;

    @ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinColumn(name="coupon_id")
    @NotNull
    private CoupanEntity coupan;

    @Column(name="discount")
    @NotNull
    private BigDecimal discount;

    @Column(name="date")
    @NotNull
    private Date date;

    @OneToOne(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name="payment_id")
    private PaymentEntity payment;

    @ManyToOne(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name="customer_id")
    @NotNull
    private CustomerEntity customer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="address_id")
    @NotNull
    private AddressEntity address;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="restaurant_id")
    @NotNull
    private RestaurantEntity restaurant;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() { return uuid; }

    public void setUuid(String uuid) { this.uuid = uuid; }

    public BigDecimal getBill() {
        return bill;
    }

    public void setBill(BigDecimal bill) {
        this.bill = bill;
    }

    public CoupanEntity getCoupan() {
        return coupan;
    }

    public void setCoupan(CoupanEntity coupan) {
        this.coupan = coupan;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public PaymentEntity getPayment() {
        return payment;
    }

    public void setPayment(PaymentEntity payment) {
        this.payment = payment;
    }

    public CustomerEntity getCustomer() { return customer; }

    public void setCustomer(CustomerEntity customer) { this.customer = customer; }

    public AddressEntity getAddress() { return address; }

    public void setAddress(AddressEntity address) { this.address = address; }

    public RestaurantEntity getRestaurant() { return restaurant; }

    public void setRestaurant(RestaurantEntity restaurant) { this.restaurant = restaurant; }

    @Override
    public boolean equals(Object obj) {
        return new EqualsBuilder().append(this, obj).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this).hashCode();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
