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

    @ManyToOne
    @JoinColumn(name="address_id")
    @NotNull
    private AddressEntity address;

    @ManyToMany
    @JoinTable(name = "restaurant_category", joinColumns = @JoinColumn(name = "restaurant_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<CategoryEntity> categories = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "restaurant_item", joinColumns = @JoinColumn(name = "restaurant_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id"))
    private List<ItemEntity> items = new ArrayList<>();

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

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

    public void setNumberOfCustomersRated(Integer numberOfCustomersRated) { this.numberOfCustomersRated = numberOfCustomersRated; }

    public AddressEntity getAddress() {
        return address;
    }

    public void setAddress(AddressEntity address) {
        this.address = address;
    }

    public List<CategoryEntity> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryEntity> categories) {
        this.categories = categories;
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
