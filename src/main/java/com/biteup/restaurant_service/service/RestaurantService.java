package com.biteup.restaurant_service.service;

import java.util.List;

import org.springframework.stereotype.Service;
import com.biteup.restaurant_service.dto.RestaurentRequestDTO;
import com.biteup.restaurant_service.dto.RestaurentResponseDTO;
import com.biteup.restaurant_service.model.Location;
import com.biteup.restaurant_service.model.Restaurant;
import com.biteup.restaurant_service.repository.RestaurantRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public RestaurentResponseDTO createRestaurant(RestaurentRequestDTO req) {

        Restaurant restaurant = new Restaurant();
        restaurant.setName(req.getName());
        restaurant.setDescription(req.getDescription());
        restaurant.setEmail(req.getEmail());
        restaurant.setPhoneNumber(req.getPhoneNumber());
        restaurant.setPlaceId(req.getPlaceId());
        restaurant.setCity(req.getCity());
        restaurant.setState(req.getState());
        restaurant.setZipCode(req.getZipCode());
        restaurant.setAddress(req.getAddress());

        Location location = new Location();
        location.setLat(req.getLatitude());
        location.setLng(req.getLongitude());
        restaurant.setLocation(location);

        Restaurant saved = restaurantRepository.save(restaurant);
        log.info("Restaurant Created Successfully");

        if (saved.getId() == null) {
            return new RestaurentResponseDTO(null, "System Error");
        }

        return new RestaurentResponseDTO("Restaurant Saved Successfully", null);
    }

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }
}
