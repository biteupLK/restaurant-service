package com.biteup.restaurant_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Setter
@Getter
public class RestaurentRequestDTO {

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
    private Double latitude;
    private Double longitude;
    private String image;

}
