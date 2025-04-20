package com.biteup.restaurant_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestaurantRequestDTO {

    private String restaurantName;
    private String address;
    private String email;
    private Double lat;
    private Double lng;
}
