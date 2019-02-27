package tests;

import com.jayway.restassured.response.Response;
import io.qameta.allure.*;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

public class UserGetMeetingTest extends TestBase {

    String meeting;
    String getMeeting;
    String getSpecificMeeting;
    String getHttpMethod;
    String httpMethod;
    String meetingID;
    String duration;
    String putMeeting;
    String putHttpMethod;
    String updateMeeting;

    DateFormat dateformat;
    Calendar calender;
    Map<String, String> header;

    @BeforeTest
    public void initialization() {

        putMeeting = jsonObject.getData("URL").get("putMeetings");
        putHttpMethod = jsonObject.getData("URL").get("putHttpMethod");
        meeting = jsonObject.getData("URL").get("meetings");
        httpMethod = jsonObject.getData("URL").get("httpMethod");
        updateMeeting = jsonObject.getData("URL").get("updateMeetings");
        header = jsonObject.getData("header");
        duration = jsonObject.getData("body").get("duration");
        dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        calender = Calendar.getInstance();
    }
    @Description("User is trying to get a meeting")
    @Epic("Smoke Tests")
    @Feature("GET Tests")
    @Severity(SeverityLevel.CRITICAL)
    @Test(priority = 1)
    public void userGetMeeting() {
        String date = dateformat.format(calender.getTime()) + " " + duration;
        calender.add(Calendar.DAY_OF_MONTH, 2);
        com.jayway.restassured.response.Response putMethod = headers(header).body(date).execute(putMeeting, putHttpMethod);

        putMethod.then().statusCode(200);
        Response getMethod = headers(header).execute(getMeeting, getHttpMethod);
        getMethod.then().statusCode(200).body("body", notNullValue());

    }
    @Description("User is trying to get specific meeting that he/she can access it")
    @Epic("Regression Tests")
    @Feature("GET Tests")
    @Severity(SeverityLevel.CRITICAL)
    @Test(priority = 2)
    public void userGetSpecificMeetings() {

        String date = dateformat.format(calender.getTime()) + " " + duration;
        Response putMethod = body(date).headers(header).execute(putMeeting, putHttpMethod);
        meetingID = putMethod.then().statusCode(200).extract().path("meetingID");
        Response getMethod = headers(header).execute(getSpecificMeeting + meetingID, getHttpMethod);
        getMethod.then().statusCode(200).body("body", hasItem(meetingID));
    }

    @Description("User is trying to get a meeting which is not exist")
    @Epic("Regression Tests")
    @Feature("GET Tests")
    @Severity(SeverityLevel.NORMAL)
    @Test(priority = 3)
    public void userGetNotExistMeeting() {

        Response getMethod = headers(header).execute(getSpecificMeeting + "123", getHttpMethod);
        getMethod.then().statusCode(404);
    }

    @Description("User is trying to get a specific meeting for anothe user")
    @Epic("Regression Tests")
    @Feature("GET Tests")
    @Severity(SeverityLevel.CRITICAL)
    @Test(priority = 4)
    public void userGetSpecificMeetingsForAnotherUser() {

        Response getMethod = headers(header).execute(getSpecificMeeting + "124", getHttpMethod);
        getMethod.then().statusCode(401);
    }

    @Description("User is trying to get this last meeting ")
    @Epic("Regression Tests")
    @Feature("GET Tests")
    @Severity(SeverityLevel.MINOR)
    @Test(priority = 5)
    public void userGetPastMeetings() {

        Response getMethod = headers(header).execute(getSpecificMeeting + "122", getHttpMethod);
        getMethod.then().statusCode(200).body(nullValue());
    }

    @Description("User is trying to get a meeting without any Authentication ")
    @Epic("Regression Tests")
    @Feature("GET Tests")
    @Severity(SeverityLevel.CRITICAL)
    @Test(priority = 6)
    public void userGetMeetingWithoutAuthentication() {

        com.jayway.restassured.response.Response response = execute(getMeeting, getHttpMethod);
        response.then().statusCode(401);
    }
}
