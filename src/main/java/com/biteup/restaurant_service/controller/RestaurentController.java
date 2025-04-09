package com.biteup.restaurant_service.controller;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biteup.restaurant_service.dto.RestaurentRequestDTO;
import com.biteup.restaurant_service.dto.RestaurentResponseDTO;
import com.biteup.restaurant_service.model.Restaurant;
import com.biteup.restaurant_service.service.RestaurantService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/restaurant")
@RequiredArgsConstructor
public class RestaurentController {

    private final RestaurantService restaurantService;
    @PostMapping
    public ResponseEntity<RestaurentResponseDTO> createProducts(
        @RequestBody RestaurentRequestDTO req
    ) {
        RestaurentResponseDTO res = restaurantService.createRestaurant(req);
        if(res.getError() != null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(res);
        }
    }

    @GetMapping
    public List<Restaurant> getAllProducts(){
        return restaurantService.getAllRestaurants();
    }
    
}

