package com.biteup.restaurant_service.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "restuarants")
public class Restaurant {

    @Id
    private String id;

    private String restaurantName;
    private String adress;
    private Integer quantity;
}
