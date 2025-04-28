package com.biteup.restaurant_service.service;

import com.biteup.restaurant_service.dto.RestaurentRequestDTO;
import com.biteup.restaurant_service.dto.RestaurentResponseDTO;
import com.biteup.restaurant_service.model.Location;
import com.biteup.restaurant_service.model.Restaurant;
import com.biteup.restaurant_service.repository.RestaurantRepository;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class RestaurantService {

  private final RestaurantRepository restaurantRepository;
  private final ImageUploaderService imageUploaderService;
  private final Storage storage;
  private final String bucketName;

  public RestaurantService(
      RestaurantRepository restaurantRepository,
      ImageUploaderService imageUploaderService,
      Storage storage,
      @Value("${gcp.bucket-name}") String bucketName) {
    this.restaurantRepository = restaurantRepository;
    this.imageUploaderService = imageUploaderService;
    this.storage = storage;
    this.bucketName = bucketName;
  }

  public RestaurentResponseDTO createRestaurant(RestaurentRequestDTO req, MultipartFile imageFile) {
    try {
      String imageUrl = imageUploaderService.uploadToGcs(bucketName, imageFile);

      Restaurant restaurant = new Restaurant();
      restaurant.setName(req.getName());
      restaurant.setDescription(req.getDescription());
      restaurant.setEmail(req.getEmail());
      restaurant.setPhoneNumber(req.getPhoneNumber());
      restaurant.setPlaceId(req.getPlaceId());
      restaurant.setCity(req.getCity());
      restaurant.setState(req.getState());
      restaurant.setZipCode(req.getZipCode());
      restaurant.setAddress(req.getAddress());
      restaurant.setImage(imageUrl); // set the uploaded image path

      Location location = new Location();
      location.setLat(req.getLatitude());
      location.setLng(req.getLongitude());
      restaurant.setLocation(location);

      Restaurant saved = restaurantRepository.save(restaurant);
      log.info("Restaurant created successfully: {}", saved);

      return new RestaurentResponseDTO("Restaurant Saved Successfully", null);

    } catch (IOException e) {
      log.error("Image upload failed", e);
      return new RestaurentResponseDTO(null, "Image upload failed");
    }
  }

  public List<Restaurant> getAllRestaurants() {
    List<Restaurant> restaurants = restaurantRepository.findAll();
    for (Restaurant restaurant : restaurants) {
      try {
        String objectName = restaurant.getImage();
        if (objectName != null && !objectName.isEmpty()) {
          String signedUrl = generateSignedUrl(objectName);
          restaurant.setSignedUrl(signedUrl);
        }
      } catch (Exception e) {
        log.error("Error generating signed URL for image: {}", restaurant.getImage(), e);
      }
    }
    return restaurants;
  }

  public Restaurant getRestaurantByEmail(String restaurantEmail) {
    Restaurant restaurant = restaurantRepository.findByEmail(restaurantEmail)
        .orElseThrow(() -> new RuntimeException("Restaurant not found for email: " + restaurantEmail));
    try {
      String objectName = restaurant.getImage();
      if (objectName != null && !objectName.isEmpty()) {
        String signedUrl = generateSignedUrl(objectName);
        restaurant.setSignedUrl(signedUrl);
      }
    } catch (Exception e) {
      log.error("Error generating signed URL for image: {}", restaurant.getImage(), e);
    }
    return restaurant;
  }

  public boolean checkIfRestaurantExists(String email) {
    return restaurantRepository.countByEmail(email) > 0;
  }

  public RestaurentResponseDTO updateRestaurant(String id, RestaurentRequestDTO req) {
    Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(id);

    if (optionalRestaurant.isPresent()) {
      Restaurant restaurant = optionalRestaurant.get();
      restaurant.setName(req.getName());
      restaurant.setDescription(req.getDescription());
      restaurant.setEmail(req.getEmail());
      restaurant.setPhoneNumber(req.getPhoneNumber());
      restaurant.setPlaceId(req.getPlaceId());
      restaurant.setCity(req.getCity());
      restaurant.setState(req.getState());
      restaurant.setZipCode(req.getZipCode());
      restaurant.setAddress(req.getAddress());

      Location location = new Location();
      location.setLat(req.getLatitude());
      location.setLng(req.getLongitude());
      restaurant.setLocation(location);

      restaurantRepository.save(restaurant);

      return new RestaurentResponseDTO("Update successful", null);
    } else {
      return new RestaurentResponseDTO(null, "Restaurant not found with id: " + id);
    }
  }

  public boolean deleteRestaurant(String id) {
    Optional<Restaurant> restaurant = restaurantRepository.findById(id);
    if (restaurant.isPresent()) {
      restaurantRepository.deleteById(id);
      return true;
    }
    return false;
  }

  private String generateSignedUrl(String objectName) {
    Blob blob = storage.get(bucketName, objectName);
    if (blob == null) {
      throw new RuntimeException("File not found in GCS: " + objectName);
    }
    URL url = blob.signUrl(1, TimeUnit.HOURS);
    return url.toString();
  }
}
