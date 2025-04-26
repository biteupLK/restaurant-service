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
  @GetMapping("/checkRestaurant/{email}")
  public ResponseEntity<Boolean> checkIfRestaurantExists(@PathVariable String email) {
      System.out.println("Checking for email: " + email); // Debug print
      boolean exists = restaurantService.checkIfRestaurantExists(email);
      return ResponseEntity.ok(exists);
  }
  
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
