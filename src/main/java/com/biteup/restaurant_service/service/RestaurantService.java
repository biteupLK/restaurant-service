package com.biteup.restaurant_service.service;

import com.biteup.restaurant_service.dto.RestaurentRequestDTO;
import com.biteup.restaurant_service.dto.RestaurentResponseDTO;
import com.biteup.restaurant_service.model.Location;
import com.biteup.restaurant_service.model.Restaurant;
import com.biteup.restaurant_service.repository.RestaurantRepository;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class RestaurantService {

  private final RestaurantRepository restaurantRepository;
  private final ImageUploaderService imageUploaderService;
  private final Storage storage;
  private final String bucketName;
  private final OkHttpClient httpClient = new OkHttpClient();

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
      restaurant.setImage(imageUrl);

      Location location = new Location();
      location.setLat(req.getLatitude());
      location.setLng(req.getLongitude());
      restaurant.setLocation(location);

      Restaurant saved = restaurantRepository.save(restaurant);
      log.info("Restaurant Created Successfully", saved);
      return new RestaurentResponseDTO("Restaurant Saved Successfully", null);

    } catch (Exception e) {
      log.error("Error while creating restaurant: {}", e.getMessage());
      return new RestaurentResponseDTO(null, "System Error");
    }
  }

  public String generateShortenedSignedUrl(String objectName) throws Exception {
    Blob blob = storage.get(bucketName, objectName);
    if (blob == null) {
      throw new RuntimeException("File not found in GCS: " + objectName);
    }

    URL signedUrl = blob.signUrl(7, TimeUnit.DAYS);
    String longUrl = signedUrl.toString();

    // Step 2: Call TinyURL to shorten it
    String tinyUrlApi = "https://tinyurl.com/api-create.php?url=" + longUrl;
    Request request = new Request.Builder().url(tinyUrlApi).build();

    try (Response response = httpClient.newCall(request).execute()) {
      if (!response.isSuccessful()) {
        throw new RuntimeException("Failed to shorten URL: " + response);
      }
      return response.body().string(); // This is the shortened URL
    }
  }

  public List<Restaurant> getAllRestaurants() {
    return restaurantRepository.findAll();
  }

  public Restaurant getRestaurentByEmail(String restaurantEmail) {
    return restaurantRepository
        .findByEmail(restaurantEmail)
        .orElseThrow(() -> new RuntimeException("Restaurant not found for id: " + restaurantEmail));
  }

  public RestaurentResponseDTO updateRestaurant(String email, RestaurentRequestDTO req, MultipartFile imageFile) {
    try {
      Optional<Restaurant> optionalRestaurant = restaurantRepository.findByEmail(email);

      if (!optionalRestaurant.isPresent()) {
        return RestaurentResponseDTO.builder()
            .error("Restaurant not found with id: " + email)
            .build();
      }

      Restaurant restaurant = optionalRestaurant.get();
      restaurant.setName(req.getName());
      restaurant.setDescription(req.getDescription());
      restaurant.setPhoneNumber(req.getPhoneNumber());
      restaurant.setPlaceId(req.getPlaceId());
      restaurant.setCity(req.getCity());
      restaurant.setState(req.getState());
      restaurant.setZipCode(req.getZipCode());
      restaurant.setAddress(req.getAddress());

      // Update location
      Location location = new Location();
      location.setLat(req.getLatitude());
      location.setLng(req.getLongitude());
      restaurant.setLocation(location);

      // Update image if new file is provided
      if (imageFile != null && !imageFile.isEmpty()) {
        String imageUrl = imageUploaderService.uploadToGcs(bucketName, imageFile);
        restaurant.setImage(imageUrl);
      }

      restaurantRepository.save(restaurant);

      return RestaurentResponseDTO.builder()
          .message("Update successful")
          .build();
    } catch (Exception e) {
      log.error("Error while updating restaurant: {}", e.getMessage());
      return RestaurentResponseDTO.builder()
          .error("System Error during update")
          .build();
    }
  }

  public boolean deleteRestaurant(String email) {
    Optional<Restaurant> restaurant = restaurantRepository.findByEmail(email);
    if (restaurant.isPresent()) {
      restaurantRepository.delete(restaurant.get()); 
      return true;
    }
    return false;
  }

  public boolean checkIfRestaurantExists(String email) {
    return restaurantRepository.findByEmail(email).isPresent();
  }

  // public List<Restaurant> getRestaurantLogo(String email) {
  // List<Restaurant> restaurants = restaurantRepository.findAllByEmail(email);
  // for (Restaurant restaurant : restaurants){
  // try{
  // String objectName = restaurant.getImage();
  // String signedUrl = generateShortenedSignedUrl(objectName);
  // restaurant.setSignedUrl(signedUrl);
  // } catch (Exception e){
  // log.error("Error generating signed URL: {}", e.getMessage());
  // }
  // }
  // return restaurants;
  // }

  public Restaurant getRestaurantByEmailImg(String email) {
    Restaurant restaurant = restaurantRepository
        .findByEmail(email)
        .orElseThrow(() -> new RuntimeException("Restaurant not found for email: " + email));

    try {
      String objectName = restaurant.getImage();
      if (objectName != null && !objectName.isEmpty()) {
        String signedUrl = generateShortenedSignedUrl(objectName);
        restaurant.setSignedUrl(signedUrl);
      }
    } catch (Exception e) {
      log.error("Error generating signed URL: {}", e.getMessage());
    }
    return restaurant;

  }

}
