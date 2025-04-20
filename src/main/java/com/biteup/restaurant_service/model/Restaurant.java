package com.biteup.restaurant_service.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "restaurants")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Restaurant {

    @Id
    private String id;

    private String name;
    private String description;
    private String address;
    private String email;
    private String phoneNumber;
    private String placeId;
    private String city;
    private String state;
    private String zipCode;
    private Object logo;

    private Location location;
}
