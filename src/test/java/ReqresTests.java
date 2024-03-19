import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class ReqresTests {
    @Test
    @DisplayName("Проверка пользователья по id")
    void checkUserNameById() {
        given()
                .log().all()
                .when()
                .get("https://reqres.in/api/users/2")
                .then()
                .log().all()
                .statusCode(200)
                .body("data.first_name", is("Janet"));
    }

    @Test
    @DisplayName("Проверка что пользователь не найден")
    void checkUserNotFound() {
        given()
                .log().all()
                .when()
                .get("https://reqres.in/api/users/23")
                .then()
                .log().all()
                .statusCode(404);
    }

    @Test
    @DisplayName("Проверка создания пользователя")
    void checkCreateUser() {
        String body = "{ \"name\": \"Smith\", \"job\": \"agent\"}";
        Response response = given()
                .log().uri()
                .contentType(JSON)
                .body(body)
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .body(matchesJsonSchemaInClasspath("schemas/user-schema.json"))
                .extract().response();
                assertThat(response.path("name"), equalTo("Smith"));
                assertThat(response.path("job"), equalTo("agent"));

    }

    @Test
    @DisplayName("Проверка обновления данных пользователя")
    void checkUpdateUser() {
        String body = "{ \"name\": \"Erlond\", \"job\": \"rivindel president\"}";
        Response response = given()
                .log().uri()
                .contentType(JSON)
                .body(body)
                .when()
                .put("https://reqres.in/api/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract().response();
                assertThat(response.path("name"), equalTo("Erlond"));
                assertThat(response.path("job"), equalTo("rivindel president"));

    }

    @Test
    @DisplayName("Проверка удаления пользователя")
    void checkDeleteUser() {
        given()
                .log().all()
                .when()
                .delete("https://reqres.in/api/users/2")
                .then()
                .log().all()
                .statusCode(204);
    }
}
