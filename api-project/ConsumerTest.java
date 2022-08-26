package liveProject;

import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

@ExtendWith(PactConsumerTestExt.class)
public class ConsumerTest {
    //Headers
    Map<String,String> headers = new HashMap<>();
    //Resource path
    String resourcePath = "/api/users";

    //Create Pact
    @Pact(consumer = "UserConsumer", provider = "UserProvider")
    public RequestResponsePact createPact(PactDslWithProvider builder) {
        //Headers
        headers.put("Content-Type", "application/json");

        //Request and Response body(same for POST request)
        DslPart requestResponseBody = new PactDslJsonBody()
                .numberType("id",1234)
                .stringType("firstName", "Ash")
                .stringType("lastName", "Dabbe")
                .stringType("email", "ashdab@gmail.com");

        //Pact(Provider/server perspective)
        return builder.given("Request to create a User")
                .uponReceiving("Request to create a User")
                  .method("POST")
                  .path(resourcePath)
                  .headers(headers)
                  .body(requestResponseBody)
                .willRespondWith()
                  .status(201)
                  .body(requestResponseBody)//same as it is a post request
                .toPact();
    }

    @Test
    @PactTestFor(providerName="UserProvider",port="8282")//Create mock server
    public void postRequestTest(){
        //Mock Server URL
        String mockServer = "http://localhost:8282";
        //request body
        Map<String,Object> reqBody = new HashMap<>();
        reqBody.put("id",999);
        reqBody.put("firstName","Chikki");
        reqBody.put("lastName","Mimi");
        reqBody.put("email","ashdabbe@gmail.com");

        //Generate response
        given().body(reqBody).headers(headers)
                .when().post(mockServer + resourcePath)
                .then().statusCode(201).log().all();

    }

}
