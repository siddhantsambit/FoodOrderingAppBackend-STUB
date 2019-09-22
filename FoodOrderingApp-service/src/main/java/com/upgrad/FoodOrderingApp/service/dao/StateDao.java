package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;

@Repository
public class StateDao {

    @Autowired
    private EntityManager entityManager;

    /* get state from the database on the basis of state uuid*/
    public StateEntity getStateByUUID(String uuid) {
        try {
            return entityManager.createNamedQuery("stateByUuid", StateEntity.class).setParameter("uuid", uuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /* method to retrieve list of all states from the database */
    public List<StateEntity> getAllStates() {
        try {
            return entityManager.createNamedQuery("getAllStates", StateEntity.class).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }
}
