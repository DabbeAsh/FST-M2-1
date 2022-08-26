package example;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;

public class CodeReuseExample {
    RequestSpecification requestSpec;
    ResponseSpecification responseSpec;

    int petId;

    @BeforeClass
    public void setUp(){
        //Request Spec
        requestSpec = new RequestSpecBuilder()
                .setBaseUri("https://petstore.swagger.io/v2/pet")
                .setContentType(ContentType.JSON)
                .build();

        //Response spec
        responseSpec = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectBody("status" , equalTo("alive"))
                .expectResponseTime(lessThan(5000L))
                .build();



        }

        @Test(priority=1)
        public void postRequestTest(){
        //Request Body
            Map<String,Object> reqBody = new HashMap<>();
            reqBody.put("id","1234");
            reqBody.put("name","Iggi");
            reqBody.put("status","alive");

         //1.Full test
         given().log().all().spec(requestSpec).body(reqBody)
                 .when().post()
                 .then().spec(responseSpec);
         //2.Generate a response
         Response response = (Response) given().spec(requestSpec).body(reqBody)
                 .when().post()
                 .then().spec(responseSpec);
         //Extract specific field value from body
         petId = response.then().extract().body().path("id");
         System.out.println(petId);
         System.out.println(response.getBody().asPrettyString());
        }

        @Test(priority=2)
        public void getRequestTest(){
        //Generate response
            given().spec(requestSpec).log().all().pathParam("petId", petId)
                    .when().get("/{petId}")
                    .then().spec(responseSpec).log().all();
        }

    @Test(priority=3)
    public void deleteRequestTest(){
        //Generate response
        given().spec(requestSpec).log().all().pathParam("petId", petId)
                .when().delete("/{petId}")
                .then().log().all().statusCode(200).body("message",equalTo(petId));
    }
    }
