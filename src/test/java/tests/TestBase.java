package tests;

import Data.ParsingToJSON;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import org.testng.annotations.BeforeClass;

import java.util.Map;

import static com.jayway.restassured.RestAssured.given;

public class TestBase {

 ParsingToJSON jsonObject;
  Object body;
    String basePath;
  String baseURI;
    String cookie;
    Map<String, String> path, query, header;



    public TestBase headers(Map<String, String> headers) {
        this.header = headers;
        return this;
    }
    public TestBase Path(Map<String, String> path) {
        this.path = path;
        return this;
    }
    public TestBase body(Object body) {
        this.body = body;
        return this;
    }
    public TestBase query(Map<String, String> query) {
        this.query = query;
        return this;
    }
    public TestBase cookie(String cookie) {
        this.cookie = cookie;
        return this;
    }

    @BeforeClass
    public void initialiseApiURL() {
        String className = this.getClass().getSimpleName();
        jsonObject = new ParsingToJSON(className);
        RestAssured.baseURI = baseURI;
        RestAssured.basePath = basePath;

    }

    public Response execute(String responsePath, String httpMethod) {

        Response response;

        //Get Method

        if (httpMethod.equals("GET")) {

            response = given().pathParams(path).queryParams(query).cookie(cookie).headers(header).log().all().when().get(responsePath);
        }
        //Put Method
        else if (httpMethod.equals("PUT")) {

            response = given().pathParams(path).queryParams(query).cookie(cookie).headers(header).body(body).log().all().when().put(responsePath);

        }
        //POST Method
        else if (httpMethod.equals("POST")) {

            response = given().pathParams(path).queryParams(query).cookie(cookie).headers(header).body(body).log().all().when().post(responsePath);

        }
        //Delete Method
        else {

            response = given().pathParams(path).queryParams(query).cookie(cookie).headers(header).log().all().when().delete(responsePath);
        }
        return response;
    }



}
