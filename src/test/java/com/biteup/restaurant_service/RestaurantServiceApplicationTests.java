package com.biteup.restaurant_service;

import static org.hamcrest.Matchers.*;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MongoDBContainer;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RestaurantServiceApplicationTests {

  @ServiceConnection
  static MongoDBContainer mongoDBContainer = new MongoDBContainer(
    "mongo:7.0.5"
  );

  @LocalServerPort
  private Integer port;

  @BeforeEach
  void setup() {
    RestAssured.baseURI = "http://localhost";
    RestAssured.port = port;
  }

  static {
    mongoDBContainer.start();
  }

  @Test
  void shouldCreateRestaurant() {
    String requestBody =
      """
      {
      	"restaurantName": "Pizza Palace",
      	"adress": "123 Main Street, New York, NY",
      	"quantity": 5
      }
      """;

    RestAssured.given()
      .contentType(ContentType.JSON)
      .body(requestBody)
      .when()
      .post("/api/restaurant/create")
      .then()
      .statusCode(201)
      .body("message", equalTo("Restaurant Saved Successfully")) // Check if message is correct
      .body("error", equalTo(null)); // Check if error is null
  }

  @Test
  void shouldCreateRestaurantWithFullData() {
    String requestBody =
      """
      {
          "name": "Sushi World",
          "description": "Authentic Japanese cuisine",
          "address": "456 Ocean Ave, San Francisco, CA",
          "email": "contact@sushiworld.com",
          "phoneNumber": "1234567890",
          "placeId": "abc123",
          "city": "San Francisco",
          "state": "CA",
          "zipCode": "94123",
          "logo": null,
          "location": {
              "lat": 37.7749,
              "lng": -122.4194
          }
      }
      """;

    RestAssured.given()
      .contentType(ContentType.JSON)
      .body(requestBody)
      .when()
      .post("/api/restaurant/create")
      .then()
      .statusCode(201)
      .body("message", equalTo("Restaurant Saved Successfully"))
      .body("error", equalTo(null));
  }

  @Test
  void shouldCreateRestaurantWithOnlyName() {
    String requestBody =
      """
      {
          "name": "Only Name Restaurant"
      }
      """;

    RestAssured.given()
      .contentType(ContentType.JSON)
      .body(requestBody)
      .when()
      .post("/api/restaurant/create")
      .then()
      .statusCode(201)
      .body("message", equalTo("Restaurant Saved Successfully"))
      .body("error", equalTo(null));
  }

  @Test
  void shouldFailWhenNameIsMissing() {
    String requestBody =
      """
      {
          "address": "789 No Name St, NY"
      }
      """;

    RestAssured.given()
      .contentType(ContentType.JSON)
      .body(requestBody)
      .when()
      .post("/api/restaurant/create")
      .then()
      .statusCode(201)
      .body("message", equalTo(null))
      .body("error", equalTo("Restaurant Name is Null"));
  }

  @Test
  void shouldFailWithInvalidLocation() {
    String requestBody =
      """
      {
          "name": "Broken Location",
          "location": "not-an-object"
      }
      """;

    RestAssured.given()
      .contentType(ContentType.JSON)
      .body(requestBody)
      .when()
      .post("/api/restaurant/create")
      .then()
      .statusCode(400)
      .body("error", notNullValue());
  }

  @Test
  void shouldFailWithEmptyBody() {
    String requestBody = "{}";

    RestAssured.given()
      .contentType(ContentType.JSON)
      .body(requestBody)
      .when()
      .post("/api/restaurant/create")
      .then()
      .statusCode(201)
      .body("error", notNullValue());
  }

  @Test
  void shouldReturnAllRestaurants() {
    RestAssured.given()
      .when()
      .get("/api/restaurant")
      .then()
      .statusCode(400)
      .contentType(ContentType.JSON)
      .body("$", not(empty()));
  }

  @Test
  void eachRestaurantShouldContainValidFields() {
    RestAssured.given()
      .when()
      .get("/api/restaurant")
      .then()
      .statusCode(400)
      .body("size()", greaterThan(0))
      .body("[0].name", notNullValue())
      .body("[0].address", notNullValue());
  }

  @Test
  void shouldUpdateRestaurantSuccessfully() {
    String id = "6807c2ba8ac7ec091172a8cd"; // Replace with real one or create before test

    String requestBody =
      """
        {
          "name": "Updated Name",
          "description": "Updated Description",
          "email": "updated@email.com",
          "phoneNumber": "9999999999",
          "placeId": "xyz456",
          "city": "Updated City",
          "state": "Updated State",
          "zipCode": "99999",
          "address": "New Address",
          "latitude": 40.7128,
          "longitude": -74.0060
        }
      """;

    RestAssured.given()
      .contentType(ContentType.JSON)
      .body(requestBody)
      .when()
      .put("/api/restaurant/" + id)
      .then()
      .statusCode(200)
      .body("message", equalTo("Update Successfully"))
      .body("error", nullValue());
  }
}
