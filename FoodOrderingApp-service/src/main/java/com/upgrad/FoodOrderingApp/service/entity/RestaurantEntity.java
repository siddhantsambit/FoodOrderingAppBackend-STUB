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
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="restaurant", schema = "restaurantdb")
public class RestaurantEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="uuid", unique = true)
    @NotNull
    @Size(max=200)
    private String UUID;

    @Column(name="restaurant_name")
    @NotNull
    @Size(max=50)
    private String RestaurnatName;

    @Column(name="photo_url")
    @Size(max=255)
    private String photoUrl;

    @Column(name="customer_rating")
    @NotNull
    private BigDecimal customerRating;

    @Column(name="average_price_for_two")
    @NotNull
    private Integer averagePriceFortwo;

    @Column(name="number_of_customers_rated", columnDefinition = "integer default 0")
    @NotNull
    private Integer numberOfCustomersRated;

    @OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @JoinColumn(name="address_id")
    private AddressEntity addressOfRestaurant;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<CategoryEntity> catogeries = new ArrayList<>();

    @OneToMany(mappedBy = "orderRestaurant", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OrderEntity> restaurantOrder = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ItemEntity> items = new ArrayList<>();

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

    public String getRestaurnatName() {
        return RestaurnatName;
    }

    public void setRestaurnatName(String restaurnatName) {
        RestaurnatName = restaurnatName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public BigDecimal getCustomerRating() {
        return customerRating;
    }

    public void setCustomerRating(BigDecimal customerRating) {
        this.customerRating = customerRating;
    }

    public Integer getAveragePriceFortwo() {
        return averagePriceFortwo;
    }

    public void setAveragePriceFortwo(Integer averagePriceFortwo) {
        this.averagePriceFortwo = averagePriceFortwo;
    }

    public Integer getNumberOfCustomersRated() {
        return numberOfCustomersRated;
    }

    public void setNumberOfCustomersRated(Integer numberOfCustomersRated) {
        this.numberOfCustomersRated = numberOfCustomersRated;
    }

    public AddressEntity getAddressOfRestaurant() {
        return addressOfRestaurant;
    }

    public void setAddressOfRestaurant(AddressEntity addressOfRestaurant) {
        this.addressOfRestaurant = addressOfRestaurant;
    }

    public List<CategoryEntity> getCatogeries() {
        return catogeries;
    }

    public void setCatogeries(List<CategoryEntity> catogeries) {
        this.catogeries = catogeries;
    }

    public List<OrderEntity> getRestaurantOrder() {
        return restaurantOrder;
    }

    public void setRestaurantOrder(List<OrderEntity> restaurantOrder) {
        this.restaurantOrder = restaurantOrder;
    }

    public List<ItemEntity> getItems() {
        return items;
    }

    public void setItems(List<ItemEntity> items) {
        this.items = items;
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
