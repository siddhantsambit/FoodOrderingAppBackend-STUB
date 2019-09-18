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
    private int id;

    @Column(name="uuid")
    @NotNull
    @Size(max=200)
    private String UUID;

    @Column(name="bill")
    private BigDecimal bill;

    @OneToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinColumn(name="coupon_id")
    private CoupanEntity coupan;


    @Column(name="discount")
    private BigDecimal discount;

//    @Column(name="date")
//    private Date date;

    @Temporal(TemporalType.DATE)
    @Column(name="date")
    @NotNull
    private Date date;

    @OneToOne(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name="payment_id")
    private PaymentEntity payment;

    @ManyToOne(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name="customer_id")
    private CustomerEntity customerMappedInOrder;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="address_id")
    private AddressEntity addressOfOrders;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="restaurant_id")
    private RestaurantEntity orderRestaurant;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

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

    public CustomerEntity getCustomerMappedInOrder() {
        return customerMappedInOrder;
    }

    public void setCustomerMappedInOrder(CustomerEntity customerMappedInOrder) {
        this.customerMappedInOrder = customerMappedInOrder;
    }

    public AddressEntity getAddressOfOrders() {
        return addressOfOrders;
    }

    public void setAddressOfOrders(AddressEntity addressOfOrders) {
        this.addressOfOrders = addressOfOrders;
    }

    public RestaurantEntity getOrderRestaurant() {
        return orderRestaurant;
    }

    public void setOrderRestaurant(RestaurantEntity orderRestaurant) {
        this.orderRestaurant = orderRestaurant;
    }

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
