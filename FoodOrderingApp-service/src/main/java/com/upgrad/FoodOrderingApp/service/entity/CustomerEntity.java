package com.upgrad.FoodOrderingApp.service.entity;

import org.apache.commons.lang3.builder.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="customer", schema = "restaurantdb")
public class CustomerEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="uuid", unique = true)
    @NotNull
    @Size(max=200)
    private String uuid;

    @Column(name="firstname")
    @Size(max=30)
    @NotNull
    private String firstName;

    @Column(name="lastname")
    @Size(max=30)
    private String lastName;

    @Column(name="email")
    @Size(max=50)
    private String email;

    @Column(name="contact_number", unique = true)
    @Size(max=30)
    @NotNull
    private String contactNumber;

    @Column(name="password")
    @NotNull
    @Size(max=255)
    private String password;

    @Column(name="salt")
    @NotNull
    @Size(max=255)
    private String salt;

    @ManyToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    private List<AddressEntity> address = new ArrayList<>();

    @OneToMany(mappedBy = "customerMappedInOrder", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<OrderEntity> customerOrders = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public List<AddressEntity> getAddress() {
        return address;
    }

    public void setAddress(List<AddressEntity> address) {
        this.address = address;
    }

    public List<OrderEntity> getCustomerOrders() {
        return customerOrders;
    }

    public void setCustomerOrders(List<OrderEntity> customerOrders) {
        this.customerOrders = customerOrders;
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


