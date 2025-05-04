package com.biteup.restaurant_service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.biteup.restaurant_service.dto.RestaurentRequestDTO;
import com.biteup.restaurant_service.dto.RestaurentResponseDTO;
import com.biteup.restaurant_service.model.Restaurant;
import com.biteup.restaurant_service.repository.RestaurantRepository;
import com.biteup.restaurant_service.service.RestaurantService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TestRestaurantServiceFunction {

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private RestaurantService restaurantService;

    @Test
    public void testCreateRestaurant_success() {
        // Arrange
        RestaurentRequestDTO request = new RestaurentRequestDTO();
        request.setName("Pizza Place");
        request.setDescription("Best Pizza");
        request.setEmail("pizza@example.com");
        request.setPhoneNumber("1234567890");
        request.setPlaceId("place123");
        request.setCity("CityX");
        request.setState("StateY");
        request.setZipCode("12345");
        request.setAddress("123 Pizza Street");
        request.setLatitude(10.0);
        request.setLongitude(20.0);

        Restaurant savedRestaurant = new Restaurant();
        savedRestaurant.setId("hasyudgyaug"); // Make sure to set an ID to avoid the "System Error" branch
        
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(savedRestaurant);

        // Act
        RestaurentResponseDTO response = restaurantService.createRestaurant(request);

        // Assert
        assertEquals("Restaurant Saved Successfully", response.getMessage());
        assertNull(response.getError());
    }
    
    @Test
    public void testCreateRestaurant_unsuccess() {
        // Arrange
        RestaurentRequestDTO request = new RestaurentRequestDTO();
        request.setDescription("Best Pizza");
        request.setEmail("pizza@example.com");
        request.setPhoneNumber("1234567890");
        request.setPlaceId("place123");
        request.setCity("CityX");
        request.setState("StateY");
        request.setZipCode("12345");
        request.setAddress("123 Pizza Street");
        request.setLatitude(10.0);
        request.setLongitude(20.0);

        Restaurant savedRestaurant = new Restaurant();
        savedRestaurant.setId("hasyudgyaug"); // Make sure to set an ID to avoid the "System Error" branch
        
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(savedRestaurant);

        // Act
        RestaurentResponseDTO response = restaurantService.createRestaurant(request);

        // Assert
        assertEquals("Restaurant Saved Successfully", response.getMessage());
        assertNull(response.getError());
    }
}