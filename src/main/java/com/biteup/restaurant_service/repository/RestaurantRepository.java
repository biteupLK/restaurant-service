package com.biteup.restaurant_service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.biteup.restaurant_service.model.Restaurant;

public interface RestaurantRepository extends MongoRepository<Restaurant, String>{

}
