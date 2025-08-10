package Requests;
import POJO.Data;
import POJO.booking;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;
import io.restassured.path.json.JsonPath;
import io.restassured.http.ContentType;
import org.testng.annotations.*;

public class Auth {

    private static int bookingid;
    private static String  token;
    private static String firstname;
    private static String lastname;
    @Test(priority = 1)
    public void GetToken()
    {
        String payload = """
                  {
                      "username" : "admin",
                      "password" : "password123"
                  }
                """;

        String response =
                given()
                        .baseUri("https://restful-booker.herokuapp.com")
                        .contentType(ContentType.JSON)
                        .body(payload)
                        .when()
                        .post("/auth")
                        .then()
                        .statusCode(200)
                        .extract()
                        .asString();

        token = new JsonPath(response).getString("token");
        System.out.println("Your Token is: " + token);
    }



    @Test(priority = 2)

    public void getBookingIds_UsingAuth()

    {
        given()
                .baseUri("https://restful-booker.herokuapp.com")
                .auth()
                .preemptive()
                .basic("admin", "password123")
                .contentType(ContentType.JSON)
                .when()
                .get("/booking")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("bookingid", Integer.class);
    }

        @Test(priority = 3)
    public void CreateBooking (){

            booking B = Data.Body();

            String response =
                    given()
                            .baseUri("https://restful-booker.herokuapp.com")
                            .contentType(ContentType.JSON)
                            .body(B)
                            .when()
                            .post("/booking")
                            .then()
                            .statusCode(200)
                            .extract().asString();

            JsonPath js = new JsonPath(response);
            bookingid = js.getInt("bookingid");
            System.out.println("Your bookingid is: " + bookingid);
        }

    @Test(priority = 4,dependsOnMethods = "CreateBooking")
    public void GetBookingById (){

        String response =
                given()
                        .baseUri("https://restful-booker.herokuapp.com")
                        .header("Accept", "application/json")
                        .pathParam("id", bookingid)
                        .when()
                        .get("/booking/{id}")
                        .then()
                        .statusCode(200)
                        .extract()
                        .asString();
        System.out.println("Your Booking is: " + response);
    }

    @Test(priority = 5,dependsOnMethods = {"GetToken", "CreateBooking"}) // for the id and the token
    public void UpdateBooking()
    {
       // booking B = Data.Body();
        UpdateBooking.booking b = UpdateBooking.Data.Body();

        String response =
                given()
                        .baseUri("https://restful-booker.herokuapp.com")
                        .header("Accept", "application/json")
                        .contentType(ContentType.JSON)
                        .header("Cookie", "token=" + token)
                        .pathParam("id", bookingid)
                        .body(b)
                        .when()
                        .put("/booking/{id}")
                        .then()
                        .log().ifValidationFails()
                        .statusCode(200)
                        .extract()
                        .asString();

        JsonPath js = new JsonPath(response);
        firstname = js.getString("firstname");
        lastname  = js.getString("lastname");

        System.out.println("Your Updated Booking is: " + response);
        System.out.println("Your firstname is: " + firstname);
        System.out.println("Your lastname is: " + lastname);
    }


    @Test(priority = 6,dependsOnMethods={"UpdateBooking"})
    public void GetBookingIdByName()
    {
        java.util.List<Integer> response =
                given()
                        .baseUri("https://restful-booker.herokuapp.com")
                        .header("Accept", "application/json")
                        .queryParam("firstname", firstname)
                        .queryParam("lastname",lastname)
                        .when()
                        .get("/booking")
                        .then()
                        .statusCode(200)
                        .extract()
                        .jsonPath()
                        .getList("bookingid", Integer.class);

        System.out.println("Matching booking IDs: " + response);
        org.testng.Assert.assertTrue(
                response.contains(bookingid),
                "Created bookingId " + bookingid + " was not returned by the name filter!"
        );
    }
@Test(priority = 7,dependsOnMethods = {"GetToken", "CreateBooking"})
public void UpdatePartial()
{
    String payload = """
                  {
                  "firstname" : "Jolie",
                  "lastname" : "Hanna"
              }
                """;
    String response =
            given()
                    .baseUri("https://restful-booker.herokuapp.com")
                    .header("Accept", "application/json")
                    .contentType(ContentType.JSON)
                    .header("Cookie", "token=" + token)
                    .pathParam("id", bookingid)
                    .body(payload)
                    .when()
                    .patch("/booking/{id}")
                    .then()
                    .log().ifValidationFails()
                    .statusCode(200)
                    .extract()
                    .asString();
    System.out.println(response);
}

@Test(priority = 8 ,dependsOnMethods = {"GetToken", "CreateBooking"})
   public void DeleteBooking()
{
    String response =
            given()
                    .baseUri("https://restful-booker.herokuapp.com")
                    .header("Accept", "text/plain")
                    .cookie("token", token)
                    .pathParam("id", bookingid)
                    .when()
                    .delete("/booking/{id}")
                    .then()
                    .log()
                    .all()
                    .statusCode(201)
                    .body(equalTo("Created")).toString();
    System.out.println(response);

}
}

