import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RestfulBooker {

    @Test
    public void Delete()
    {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";


        Map<String, String> authPayload = new HashMap<>();
        authPayload.put("username", "admin");
        authPayload.put("password", "password123");

        Response authResponse = given()
                .contentType(ContentType.JSON)
                .body(authPayload)
                .when()
                .post("/auth");

        authResponse.then().assertThat().statusCode(200).body("token", notNullValue());


        String token = authResponse.jsonPath().getString("token");
        assertNotNull("Token no debería ser null", token);

        System.out.println("Token generado: " + token);


        Response deleteResponse = given()
                .pathParam("id", "1")
                .contentType(ContentType.JSON)
                .header("Cookie", "token=" + token)
                .when()
                .delete("/booking/{id}");
        deleteResponse.then().assertThat().statusCode(201);
    }

    @Test
    public void createBooking()//POST
    {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";


        Map<String, String> authPayload = new HashMap<>();
        authPayload.put("username", "admin");
        authPayload.put("password", "password123");

        Response authResponse = given()
                .contentType(ContentType.JSON)
                .body(authPayload)
                .when()
                .post("/auth");


        authResponse.then().assertThat().statusCode(200).body("token", notNullValue());

        // Extraer el token de la respuesta
        String token = authResponse.jsonPath().getString("token");
        assertNotNull("Token no debería ser null", token);

        // Imprimir el token en la consola para depuración
        System.out.println("Token generado: " + token);


        Map<String, Object> bookingDetails = new HashMap<>();
        bookingDetails.put("firstname", "Natalia");
        bookingDetails.put("lastname", "Arze");
        bookingDetails.put("totalprice", 1500);
        bookingDetails.put("depositpaid", true);

        Map<String, String> bookingDates = new HashMap<>();
        bookingDates.put("checkin", "2025-09-01");
        bookingDates.put("checkout", "2025-09-10");

        bookingDetails.put("bookingdates", bookingDates);
        bookingDetails.put("additionalneeds", "Breakfast");

        Response postResponse = given()
                .contentType(ContentType.JSON)
                .header("Cookie", "token=" + token)
                .body(bookingDetails)
                .when()
                .post("/booking");


        postResponse.then().assertThat().statusCode(200).body("bookingid", notNullValue());
    }

    @Test
    public void putBooking()
    {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";

        Map<String, String> authPayload = new HashMap<>();
        authPayload.put("username", "admin");
        authPayload.put("password", "password123");

        Response authResponse = given()
                .contentType(ContentType.JSON)
                .body(authPayload)
                .when()
                .post("/auth");

        authResponse.then().assertThat().statusCode(200).body("token", notNullValue());


        String token = authResponse.jsonPath().getString("token");
        assertNotNull("Token no debería ser null", token);


        System.out.println("Token generado: " + token);

        Map<String, Object> updatedBookingDetails = new HashMap<>();
        updatedBookingDetails.put("firstname", "Jane");
        updatedBookingDetails.put("lastname", "Doe");
        updatedBookingDetails.put("totalprice", 200);
        updatedBookingDetails.put("depositpaid", false);

        Map<String, String> updatedBookingDates = new HashMap<>();
        updatedBookingDates.put("checkin", "2023-10-01");
        updatedBookingDates.put("checkout", "2023-10-10");

        updatedBookingDetails.put("bookingdates", updatedBookingDates);
        updatedBookingDetails.put("additionalneeds", "Lunch");


        Response putResponse = given()
                .contentType(ContentType.JSON)
                .header("Cookie", "token=" + token)
                .pathParam("id", "10")   // Especificar el id del booking a actualizar
                .body(updatedBookingDetails) // Cuerpo de la solicitud con detalles actualizados del booking
                .when()
                .put("/booking/{id}");


        putResponse.then().assertThat().statusCode(200)
                .body("firstname", equalTo("Natalia"))
                .body("lastname", equalTo("Arze"))
                .body("totalprice", equalTo(200))
                .body("depositpaid", equalTo(false))
                .body("bookingdates.checkin", equalTo("2023-10-01"))
                .body("bookingdates.checkout", equalTo("2023-10-10"))
                .body("additionalneeds", equalTo("Lunch"));

    }

    @Test
    public void CreateConNameInvalido()
    {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";


        Map<String, String> authPayload = new HashMap<>();
        authPayload.put("username", "admin");
        authPayload.put("password", "password123");

        Response authResponse = given()
                .contentType(ContentType.JSON)
                .body(authPayload)
                .when()
                .post("/auth");


        authResponse.then().assertThat().statusCode(200).body("token", notNullValue());

        // Extraer el token de la respuesta
        String token = authResponse.jsonPath().getString("token");
        assertNotNull("Token no debería ser null", token);

        // Imprimir el token en la consola para depuración
        System.out.println("Token generado: " + token);


        Map<String, Object> bookingDetails = new HashMap<>();
        bookingDetails.put("firstname", "Natalia");
        bookingDetails.put("lastname", 1212);
        bookingDetails.put("totalprice", 1500);
        bookingDetails.put("depositpaid", true);

        Map<String, String> bookingDates = new HashMap<>();
        bookingDates.put("checkin", "2025-09-01");
        bookingDates.put("checkout", "2025-09-10");

        bookingDetails.put("bookingdates", bookingDates);
        bookingDetails.put("additionalneeds", "Breakfast");

        Response postResponse = given()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .body(bookingDetails)
                .when()
                .post("/booking");


        postResponse.then().assertThat().statusCode(400).body("bookingid", notNullValue());//bad request
    }

    @Test
    public void TestGet()
    {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";

        Response response = RestAssured.given().pathParam("id", "1").when().get("/booking/{id}");

        response.then().assertThat().statusCode(200);
        response.then().assertThat().body("size()", not(0));
        response.then().assertThat().body("firstname", equalTo("Natalia"));
        response.then().assertThat().body("lastname", equalTo("Arze"));
        response.then().log().body();

    }
}
