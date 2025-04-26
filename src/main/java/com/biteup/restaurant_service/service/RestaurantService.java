package com.biteup.restaurant_service.service;

import com.biteup.restaurant_service.dto.RestaurentRequestDTO;
import com.biteup.restaurant_service.dto.RestaurentResponseDTO;
import com.biteup.restaurant_service.model.Location;
import com.biteup.restaurant_service.model.Restaurant;
import com.biteup.restaurant_service.repository.RestaurantRepository;
import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

  public Restaurant getRestaurentByEmail(String restaurantEmail) {
    return restaurantRepository
      .findByEmail(restaurantEmail)
      .orElseThrow(() ->
        new RuntimeException("Restaurant not found for id: " + restaurantEmail)
      );
  }
  public RestaurentResponseDTO updateRestaurant(String id, RestaurentRequestDTO req) {
    Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(id);

    if (optionalRestaurant.isPresent()) {
        Restaurant restaurant = optionalRestaurant.get();
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

        restaurantRepository.save(restaurant);

        return RestaurentResponseDTO.builder()
                .message("Update successful")
                .build();
    } else {
        return RestaurentResponseDTO.builder()
                .error("Restaurant not found with id: " + id)
                .build();
    }
}


    public boolean deleteRestaurant(String id) {
        Optional<Restaurant> restaurant = restaurantRepository.findById(id);
        if (restaurant.isPresent()) {
            restaurantRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
