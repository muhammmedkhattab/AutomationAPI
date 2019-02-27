package tests;

import com.jayway.restassured.response.Response;
import io.qameta.allure.*;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

public class UserPutMeetingTest extends TestBase {


    String meeting;
    String httpMethod;
    String putMeeting;
    String putHttpMethod;
    String meetingID;
    String duration;
    String deleteMeeting;
    String deleteHttpMethod;
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
        deleteMeeting = jsonObject.getData("URL").get("deleteMeetings");
        deleteHttpMethod = jsonObject.getData("URL").get("deleteHttpMethod");
        updateMeeting = jsonObject.getData("URL").get("updateMeetings");
        header = jsonObject.getData("header");
        duration = jsonObject.getData("body").get("duration");
        dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        calender = Calendar.getInstance();
    }
    @Description("User update this own meeting using his own ID")
    @Epic("Smoke Tests")
    @Feature("PUT Tests")
    @Severity(SeverityLevel.CRITICAL)
    @Test(priority = 1)
    public void userUpdateMeetingSuccessfully() {
        String date = dateformat.format(calender.getTime()) + " " + duration;
        Response putMethod = headers(header).body(date).execute(putMeeting, putHttpMethod);
        meetingID = putMethod.then().statusCode(200).extract().path("meetingID");
        Response update = headers(header).body(date).execute(updateMeeting + meetingID, putHttpMethod);
        update.then().statusCode(200);

    }
    @Description("User update another user meeting ")
    @Epic("Regression Tests")
    @Feature("PUT Tests")
    @Severity(SeverityLevel.NORMAL)
    @Test(priority = 2)
    public void userUpdateAnotherUserMeeting() {
        String date = dateformat.format(calender.getTime()) + " " + duration;
        Response update = headers(header).body(date).execute(updateMeeting + "123456", putHttpMethod);
        update.then().statusCode(401);
    }

    @Description("User update a meeting with having a permission")
    @Epic("Regression Tests")
    @Feature("PUT Tests")
    @Severity(SeverityLevel.CRITICAL)
    @Test(priority = 3)
    public void userUpdateMeetingNOPermission() {

        String date = dateformat.format(calender.getTime()) + " " + duration;
        Response putResp = body(date).execute(putMeeting, putHttpMethod);
        putResp.then().statusCode(401);
    }

    @Description("User update meeting using invalid Date ")
    @Epic("Regression Tests")
    @Feature("PUT Tests")
    @Severity(SeverityLevel.MINOR)
    @Test(priority = 4)
    public void userUpdateMeetingInvalidDate() {

        String date = dateformat.format(calender.getTime()) + " " + duration;
        calender.add(Calendar.DAY_OF_MONTH, -9);

        Response putResp = headers(header).body(date).execute(putMeeting, putHttpMethod);
        putResp.then().statusCode(400);
    }

    @Description("User update a deleted meeting")
    @Epic("Regression Tests")
    @Feature("PUT Tests")
    @Severity(SeverityLevel.CRITICAL)
    @Test(priority = 5)
    public void userUpdateDeletedMeeting() {

        String date = dateformat.format(calender.getTime()) + " " + duration;
        Response putResp = headers(header).body(date).execute(putMeeting, putHttpMethod);
        meetingID = putResp.then().statusCode(200).extract().path("meetingID");
        Response deleteRest = headers(header).execute(deleteMeeting + meetingID, deleteHttpMethod);
        deleteRest.then().statusCode(204);
        Response updateResp = headers(header).body(date).execute(updateMeeting + meetingID, putHttpMethod);
        updateResp.then().statusCode(404);
    }
    @Description("User update meeting with duplicated date")
    @Epic("Regression Tests")
    @Feature("PUT Tests")
    @Severity(SeverityLevel.NORMAL)
    @Test(priority = 6)
    public void userUpdateMeetingWithDuplicateDate() {

        String date = dateformat.format(calender.getTime()) + " " + duration;
        Response putResp = headers(header).body(date).execute(putMeeting, putHttpMethod);
        putResp.then().statusCode(409);
    }
}
