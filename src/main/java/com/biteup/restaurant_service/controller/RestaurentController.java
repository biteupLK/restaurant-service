package com.biteup.restaurant_service.controller;

import com.biteup.restaurant_service.dto.RestaurentRequestDTO;
import com.biteup.restaurant_service.dto.RestaurentResponseDTO;
import com.biteup.restaurant_service.model.Restaurant;
import com.biteup.restaurant_service.service.RestaurantService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/restaurant")
@RequiredArgsConstructor
public class RestaurentController {

  private final RestaurantService restaurantService;

  @PostMapping("/create")
  public ResponseEntity<RestaurentResponseDTO> createProducts(
    @RequestBody RestaurentRequestDTO req
  ) {
    RestaurentResponseDTO res = restaurantService.createRestaurant(req);
    if (res.getError() != null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    } else {
      return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }
  }

  @GetMapping("/check-profile/{restaurantEmail}")
  public ResponseEntity<Boolean> checkIfRestaurantNeedsForm(@PathVariable String restaurantEmail) {
      Restaurant restaurant = restaurantService.getRestaurentByEmail(restaurantEmail);
  
      boolean needsForm = false;
  
      // Check if important fields are missing
      if (restaurant.getName() == null || restaurant.getName().isEmpty()
              || restaurant.getPhoneNumber() == null || restaurant.getPhoneNumber().isEmpty()
              || restaurant.getAddress() == null || restaurant.getAddress().isEmpty()
              || restaurant.getPhoneNumber() == null || restaurant.getPhoneNumber().isEmpty()
              || restaurant.getLocation() == null
              || restaurant.getLocation().getLat() == 0.0
              || restaurant.getLocation().getLng() == 0.0) {
          needsForm = true;
      }
  
      return ResponseEntity.ok(needsForm);
  }

  // if check by restaurant name
//   @GetMapping("/check-profile/{restaurantName}")
// public ResponseEntity<Boolean> checkIfRestaurantNeedsForm(@PathVariable String restaurantName) {
//     Restaurant restaurant = restaurantService.getRestaurentByName(restaurantName);

//     boolean needsForm = false;

//     // Check if important fields are missing
//     if (restaurant.getPhoneNumber() == null || restaurant.getPhoneNumber().isEmpty()
//             || restaurant.getAddress() == null || restaurant.getAddress().isEmpty()
//             || restaurant.getLocation() == null
//             || restaurant.getLocation().getLat() == 0.0
//             || restaurant.getLocation().getLng() == 0.0) {
//         needsForm = true;
//     }

//     return ResponseEntity.ok(needsForm);
// }


  @GetMapping
  public List<Restaurant> getAllProducts() {
    return restaurantService.getAllRestaurants();
  }

  @GetMapping("/{restaurantEmail}")
  public ResponseEntity<Restaurant> getRestaurentByEmail(
    @PathVariable String restaurantEmail
  ) {
    Restaurant restaurant = restaurantService.getRestaurentByEmail(
      restaurantEmail
    );
    return ResponseEntity.ok(restaurant);
  }
  @PutMapping("/{id}")
    public ResponseEntity<RestaurentResponseDTO> updateRestaurant(
            @PathVariable String id,
            @RequestBody RestaurentRequestDTO requestDTO) {
        RestaurentResponseDTO res = restaurantService.updateRestaurant(id, requestDTO);
        if (res.getError() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
        } else {
            return ResponseEntity.ok(res);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRestaurant(@PathVariable String id) {
        boolean deleted = restaurantService.deleteRestaurant(id);
        if (deleted) {
            return ResponseEntity.ok("Restaurant deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Restaurant not found.");
        }
    }
}
