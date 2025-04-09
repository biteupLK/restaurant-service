package com.biteup.restaurant_service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.biteup.restaurant_service.dto.RestaurentRequestDTO;
import com.biteup.restaurant_service.dto.RestaurentResponseDTO;
import com.biteup.restaurant_service.model.Restaurant;
import com.biteup.restaurant_service.repository.RestaurantRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    public RestaurentResponseDTO createRestaurant(RestaurentRequestDTO req){

        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantName(req.getRestaurantName());
        restaurant.setAdress(req.getAdress());
        restaurant.setQuantity(req.getQuantity());


        Restaurant saved = restaurantRepository.save(restaurant);
        log.info("Restaurant Create Successfully");

        if (saved.getId() == null)
            return new RestaurentResponseDTO(null, "System Error");

        return new RestaurentResponseDTO("Restaurant Saved Success", null);
    }

    public List<Restaurant> getAllRestaurants(){
        return restaurantRepository.findAll();
    }
}
