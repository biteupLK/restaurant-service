package com.biteup.restaurant_service.controller;

import com.biteup.restaurant_service.dto.RestaurentRequestDTO;
import com.biteup.restaurant_service.dto.RestaurentResponseDTO;
import com.biteup.restaurant_service.model.Restaurant;
import com.biteup.restaurant_service.service.RestaurantService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/restaurant")
@RequiredArgsConstructor
public class RestaurentController {

  private final RestaurantService restaurantService;

  @GetMapping("/checkRestaurant/{email}")
  public ResponseEntity<Boolean> checkIfRestaurantExists(@PathVariable String email) {
    boolean exists = restaurantService.checkIfRestaurantExists(email);
    return ResponseEntity.ok(exists);
  }

  @GetMapping
  public List<Restaurant> getAllProducts() {
    return restaurantService.getAllRestaurants();
  }

  @GetMapping("/{restaurantEmail}")
  public ResponseEntity<Restaurant> getRestaurentByEmail(
      @PathVariable String restaurantEmail) {
    Restaurant restaurant = restaurantService.getRestaurentByEmail(
        restaurantEmail);
    return ResponseEntity.ok(restaurant);
  }

  @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<RestaurentResponseDTO> createProducts(
      @RequestPart("restaurant") String restaurantJson,
      @RequestPart("image") MultipartFile image) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();

    RestaurentRequestDTO req = objectMapper.readValue(
        restaurantJson,
        RestaurentRequestDTO.class);

    RestaurentResponseDTO res = restaurantService.createRestaurant(req, image);
    if (res.getError() != null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    } else {
      return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }
  }

  @PutMapping(value = "/{email}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<RestaurentResponseDTO> updateRestaurant(
      @PathVariable String email,
      @RequestPart(value = "restaurant", required = true) String restaurantJson,
      @RequestPart(value = "image", required = false) MultipartFile image) throws JsonProcessingException {
      
      ObjectMapper objectMapper = new ObjectMapper();
      RestaurentRequestDTO requestDTO = objectMapper.readValue(restaurantJson, RestaurentRequestDTO.class);
  
      RestaurentResponseDTO res = restaurantService.updateRestaurant(email, requestDTO, image);
      if (res.getError() != null) {
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
      } else {
          return ResponseEntity.ok(res);
      }
  }


  
  @DeleteMapping("/{email}")
  public ResponseEntity<String> deleteRestaurant(@PathVariable String email) {
    boolean deleted = restaurantService.deleteRestaurant(email);
    if (deleted) {
      return ResponseEntity.ok("Restaurant deleted successfully.");
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Restaurant not found.");
    }
  }

  @GetMapping("/getSignedUrl/{email}")
  public ResponseEntity<Restaurant> getSignedUrl(@PathVariable String email) {
    Restaurant restaurant = restaurantService.getRestaurantByEmailImg(email);
    return ResponseEntity.ok(restaurant);
  }

}
