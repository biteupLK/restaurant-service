package com.biteup.restaurant_service.controller;

import com.biteup.restaurant_service.dto.RestaurentRequestDTO;
import com.biteup.restaurant_service.dto.RestaurentResponseDTO;
import com.biteup.restaurant_service.model.Restaurant;
import com.biteup.restaurant_service.service.RestaurantService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/restaurant")
@RequiredArgsConstructor
public class RestaurentController {

    private final RestaurantService restaurantService;

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RestaurentResponseDTO> createRestaurant(
            @RequestPart("restaurant") String restaurantJson,
            @RequestPart("image") MultipartFile image
    ) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        RestaurentRequestDTO req = objectMapper.readValue(restaurantJson, RestaurentRequestDTO.class);

        RestaurentResponseDTO res = restaurantService.createRestaurant(req, image);
        if (res.getError() != null) {
            return ResponseEntity.badRequest().body(res);
        } else {
            return ResponseEntity.status(201).body(res);
        }
    }

    @GetMapping("/checkRestaurant/{email}")
    public ResponseEntity<Boolean> checkIfRestaurantExists(@PathVariable String email) {
        boolean exists = restaurantService.checkIfRestaurantExists(email);
        return ResponseEntity.ok(exists);
    }

    @GetMapping
    public List<Restaurant> getAllRestaurants() {
        return restaurantService.getAllRestaurants();
    }

    @GetMapping("/{restaurantEmail}")
    public ResponseEntity<Restaurant> getRestaurantByEmail(@PathVariable String restaurantEmail) {
        Restaurant restaurant = restaurantService.getRestaurantByEmail(restaurantEmail);
        return ResponseEntity.ok(restaurant);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurentResponseDTO> updateRestaurant(
            @PathVariable String id,
            @RequestBody RestaurentRequestDTO requestDTO
    ) {
        RestaurentResponseDTO res = restaurantService.updateRestaurant(id, requestDTO);
        if (res.getError() != null) {
            return ResponseEntity.badRequest().body(res);
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
            return ResponseEntity.status(404).body("Restaurant not found.");
        }
    }
}
