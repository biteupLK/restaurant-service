package com.biteup.restaurant_service.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.biteup.restaurant_service.model.Restaurant;

public interface RestaurantRepository extends MongoRepository<Restaurant, String>{
    Optional<Restaurant> findByEmail(String email);

    Optional<Restaurant> findByName(String restaurantName);
}
