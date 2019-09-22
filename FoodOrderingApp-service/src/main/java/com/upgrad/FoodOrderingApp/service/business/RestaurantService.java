package com.upgrad.FoodOrderingApp.service.business;

import com.upgrad.FoodOrderingApp.service.dao.CategoryDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.InvalidRatingException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


@Service
public class RestaurantService {

    @Autowired
    private RestaurantDao restaurantDao;

    @Autowired
    private CategoryDao categoryDao;

    /**
     * This method implements the business logic for 'Get All Restaurants - "/restaurant"' endpoint
     *
     *When any customer tries to access this endpoint, it should retrieve all the restaurants in order of their ratings
     * @return List<RestaurantEntity> object
     */

    public List<RestaurantEntity> restaurantsByRating() {
        return restaurantDao.restaurantsByRating();
    }

    /**
     * Returns restaurants matching to given restaurant name
     *
     *Even if there is a partial match, all the restaurants corresponding to that name should be returned in alphabetical order of their names
     *
     * @param restaurantName Restaurant name
     *
     * @return List<RestaurantEntity> object
     *
     * @throws RestaurantNotFoundException If the restaurant name field entered by the customer is empty
     */

    public List<RestaurantEntity> restaurantsByName(final String restaurantName) throws RestaurantNotFoundException {
        if(restaurantName.isEmpty()){
            throw new RestaurantNotFoundException("RNF-003", "Restaurant name field should not be empty");
        }

        List<RestaurantEntity> restaurantEntityList = restaurantDao.restaurantsByRating();
        List<RestaurantEntity> matchingRestaurantEntityList = new ArrayList<RestaurantEntity>();
        for (RestaurantEntity restaurantEntity : restaurantEntityList) {
            if (restaurantEntity.getRestaurantName().toLowerCase().contains(restaurantName.toLowerCase())) {
                matchingRestaurantEntityList.add(restaurantEntity);
            }
        }

        return matchingRestaurantEntityList;
    }


    /**
     * Returns restaurants for a given category
     *
     *If the category id entered by the customer matches any category in the database, it retrieves all the restaurants under this category in alphabetical order
     *
     * @param categoryId UUID of category
     *
     * @return List<RestaurantEntity> object
     *
     * @throws CategoryNotFoundException If the category id field entered by the customer is empty
     * @throws CategoryNotFoundException If there is no category by the uuid entered by the customer
     */

    public List<RestaurantEntity> restaurantByCategory(final String categoryId) throws CategoryNotFoundException {

        if (categoryId.equals("")) {
            throw new CategoryNotFoundException("CNF-001", "Category id field should not be empty");
        }

        CategoryEntity categoryEntity = categoryDao.getCategoryByUuid(categoryId);

        if(categoryEntity == null) {
            throw new CategoryNotFoundException("CNF-002", "No category by this id");
        }

        List<RestaurantEntity> restaurantEntityList = categoryEntity.getRestaurants();
        restaurantEntityList.sort(Comparator.comparing(RestaurantEntity::getRestaurantName));
        return restaurantEntityList;
    }

    /**
     * Returns restaurant for a given UUID
     *
     *If the restaurant id entered by the customer matches any restaurant in the database, it retrieves that restaurant’s details
     *
     * @param uuid UUID of restaurant
     *
     * @return RestaurantEntity object
     *
     * @throws RestaurantNotFoundException If the restaurant id field entered by the customer is empty
     * @throws RestaurantNotFoundException If there is no restaurant by the uuid entered by the customer
     */

    public RestaurantEntity restaurantByUUID(String uuid) throws RestaurantNotFoundException {
        if (uuid.equals("")) {
            throw new RestaurantNotFoundException("RNF-002", "Restaurant id field should not be empty");
        }

        RestaurantEntity restaurantEntity = restaurantDao.getRestaurantByUUID(uuid);

        if (restaurantEntity == null) {
            throw new RestaurantNotFoundException("RNF-001", "No restaurant by this id");
        }
        return restaurantEntity;
    }

    /**
     * Updates restaurant average customer rating and number of customers rated
     *
     * @param restaurantEntity UUID of restaurant entity
     * @param newRating Customer rating
     *
     * @return RestaurantEntity object
     *
     * @throws InvalidRatingException If the customer rating field entered by the customer is empty or is not in the range of 1 to 5
     */

    @Transactional(propagation = Propagation.REQUIRED)
    public RestaurantEntity updateRestaurantRating(RestaurantEntity restaurantEntity, Double newRating) throws InvalidRatingException {

        if (newRating < 1.0 || newRating > 5.0) {
            throw new InvalidRatingException("IRE-001", "Restaurant should be in the range of 1 to 5");
        }

        Double newAverageRating = Math.round(
                (newRating / (restaurantEntity.getNumberCustomersRated() + 1)
                        + restaurantEntity.getCustomerRating()) * 100.0) / 100.0;
        restaurantEntity.setNumberCustomersRated(restaurantEntity.getNumberCustomersRated() + 1);
        restaurantEntity.setCustomerRating(newAverageRating);

        return restaurantDao.updateRestaurantEntity(restaurantEntity);
    }
}