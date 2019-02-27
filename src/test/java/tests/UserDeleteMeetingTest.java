package tests;

import com.jayway.restassured.response.Response;
import io.qameta.allure.*;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import static org.hamcrest.Matchers.not;

public class UserDeleteMeetingTest extends TestBase {

    String meeting;
    String httpMethod;
    String putMeeting;
    String putHttpMethod;
    String getMeeting;
    String getHttpMethod;
    String meetingID;
    String deleteMeeting;
    String deleteHttpMethod;
    String updateMeeting;
    String duration;

    DateFormat dateformat;
    Calendar calender;
    Map<String, String> header;

    @BeforeTest
    public void initialization() {

        putMeeting = jsonObject.getData("URL").get("putMeetings");
        putHttpMethod = jsonObject.getData("URL").get("putHttpMethod");
        meeting = jsonObject.getData("URL").get("meetings");
        httpMethod = jsonObject.getData("URL").get("httpMethod");
        deleteMeeting = jsonObject.getData("URL").get("deleteMeetings");
        deleteHttpMethod = jsonObject.getData("URL").get("deleteHttpMethod");
        updateMeeting = jsonObject.getData("URL").get("updateMeetings");
        header = jsonObject.getData("header");
        duration = jsonObject.getData("body").get("duration");
        dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        calender = Calendar.getInstance();
    }

    @Description("")
    @Epic("Smoke Tests")
    @Feature("Delete Tests")
    @Severity(SeverityLevel.CRITICAL)
    @Test(priority = 1)
    public void userDeleteMeetingsWithValidId() {

        calender.add(Calendar.DAY_OF_MONTH, 2);
        String dateString = dateformat.format(calender.getTime()) + " " + duration;
        Response putMethod = body(dateString).headers(header).execute(putMeeting, putHttpMethod);
        meetingID = putMethod.then().statusCode(200).extract().path("meetingId");
        Response deleteMethod = headers(header).execute(deleteMeeting + meetingID, deleteHttpMethod);
        deleteMethod.then().statusCode(204);
        Response getMethod = headers(header).execute(getMeeting, getHttpMethod);
        getMethod.then().statusCode(200).body("body", not(meetingID));

    }
    @Description("User delete this own meeting using a valid ID ")
    @Epic("Regression Tests")
    @Feature("Delete Tests")
    @Severity(SeverityLevel.NORMAL)
    @Test(priority = 2)
    public void userDeleteMeetingWithInvalidId() {

        Response response = headers(header).execute(deleteMeeting + "123", deleteHttpMethod);
        response.then().statusCode(404);
    }
    @Description("User delete a meeting without any type of authentication")
    @Epic("Regression Tests")
    @Feature("Delete Tests")
    @Severity(SeverityLevel.CRITICAL)
    @Test(priority = 3)
    public void userDeleteMeetingWithoutAuthentication() {

        Response response = execute(deleteMeeting + meetingID, deleteHttpMethod);
        response.then().statusCode(401);
    }
    @Description("User delete a meeting for another user ")
    @Epic("Regression Tests")
    @Feature("Delete Tests")
    @Severity(SeverityLevel.NORMAL)
    @Test(priority = 4)
    public void userDeleteMeetingDifferentUser() {

        Response response = headers(header).execute(deleteMeeting + "122", deleteHttpMethod);
        response.then().statusCode(401);
    }

    @Description("User delete a meeting with a same ID")
    @Epic("Regression Tests")
    @Feature("Delete Tests")
    @Severity(SeverityLevel.CRITICAL)
    @Test(priority = 5)
    public void userDeleteMeetingWithSameId() {

        Response response = headers(header).execute(deleteMeeting + meetingID, deleteHttpMethod);
        response.then().statusCode(410);
    }


}
