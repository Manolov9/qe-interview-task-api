package resources

import io.restassured.response.Response
import static io.restassured.RestAssured.*

class HTTPClient {

  static Response post(String endpoint, String payload) {
    return given()
            .contentType("application/json")
            .body(payload)
            .when()
            .post(endpoint)
            .then()
            .extract()
            .response()
  }

  static Response get(String endpoint) {
    return given()
            .when()
            .get(endpoint)
            .then()
            .extract()
            .response()
  }
}
