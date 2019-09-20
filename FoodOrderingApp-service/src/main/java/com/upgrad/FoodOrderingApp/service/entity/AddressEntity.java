package com.upgrad.FoodOrderingApp.service.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="address", schema = "restaurantdb")
@NamedQueries({
        @NamedQuery(name = "addressByUUID", query = "select a from AddressEntity a where a.uuid = :uuid"),
})

public class AddressEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="uuid", unique = true)
    @NotNull
    @Size(max=200)
    private String uuid;

    @Column(name="flat_buil_number")
    @NotNull
    @Size(max=225)
    private String flatBuilNo;

    @Column(name="locality")
    @NotNull
    @Size(max=225)
    private String locality;

    @Column(name="city")
    @NotNull
    @Size(max=30)
    private String city;

    @Column(name="pincode")
    @NotNull
    @Size(max=30)
    private String pincode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="state_id")
    private StateEntity state;

    @Column(name="active", columnDefinition = "integer default 1")
    private Integer active;

    @ManyToOne
    @JoinTable(name = "customer_address", joinColumns = @JoinColumn(name = "address_id"),
            inverseJoinColumns = @JoinColumn(name = "customer_id"))
    private CustomerEntity customer;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() { return uuid; }

    public void setUuid(String uuid) { this.uuid = uuid; }

    public String getFlatBuilNo() { return flatBuilNo; }

    public void setFlatBuilNo(String flatBuilNo) { this.flatBuilNo = flatBuilNo; }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public StateEntity getState() {
        return state;
    }

    public void setState(StateEntity state) {
        this.state = state;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public CustomerEntity getCustomer() { return customer; }

    public void setCustomer(CustomerEntity customer) { this.customer = customer; }

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
