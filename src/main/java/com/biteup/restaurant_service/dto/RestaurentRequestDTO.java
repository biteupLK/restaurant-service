package com.biteup.restaurant_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RestaurentRequestDTO {

    private String restaurantName;
    private String adress;
    private Integer quantity;
}
