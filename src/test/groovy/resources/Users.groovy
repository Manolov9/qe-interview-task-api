package resources

import io.restassured.response.Response
import static io.restassured.RestAssured.*

class Users {

  static Response createUser(int userId) {
    def userPayload = TestDataBuilder.createUserPayload(userId)
    return given()
            .contentType("application/json")
            .body(userPayload)
            .when()
            .post("/users")
            .then()
            .extract()
            .response()
  }

  static Response getUserById(int userId) {
    return given()
            .when()
            .get("/users/$userId")
            .then()
            .extract()
            .response()
  }
}
