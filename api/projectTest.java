package project;

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

public class projectTest {
    RequestSpecification requestSpec;
    ResponseSpecification responseSpec;
    String key;
    int keyId;

    @BeforeClass
    public void setUp() {
        //Request Spec
        requestSpec = new RequestSpecBuilder()
                .setBaseUri("https://api.github.com")
                .setContentType(ContentType.JSON)
                .addHeader("Authorization", "token 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")
                .build();
        //Response spec
        responseSpec = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectResponseTime(lessThan(5000L))
                .build();
    }
    @Test(priority=1)
    public void postRequestTest(){
        //Request Body
        Map<String,Object> reqBody = new HashMap<>();
        reqBody.put("title","TestAPIKey");
        reqBody.put("key","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");


        //1.Full test
        /*given().log().all().spec(requestSpec).body(reqBody)
                .when().post()
                .then().spec(responseSpec);*/
        //2.Generate a response
        Response response = (Response) given().spec(requestSpec)
                .when().post()
                .then().spec(responseSpec);
        //Extract specific field value from body
        keyId = response.then().extract().body().path("id");
        System.out.println(keyId);
        System.out.println(response.getBody().asPrettyString());
    }

    @Test(priority=2)
    public void getRequestTest(){
        //Generate response
        given().spec(requestSpec).log().all().pathParam("keyId", keyId)
                .when().get(" /user/keys/{keyId}")
                .then().spec(responseSpec).log().all();
    }

    @Test(priority=3)
    public void deleteRequestTest(){
        //Generate response
        given().spec(requestSpec).log().all().pathParam("keyId", keyId)
                .when().delete(" /user/keys/{keyId}")
                .then().log().all().statusCode(200).body("message",equalTo(keyId));
    }
}
