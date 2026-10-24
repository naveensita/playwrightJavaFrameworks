package aero.sita.iborders.tests;

import com.jayway.jsonpath.JsonPath;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;

public class ApiTest {

    @Test(groups = "Smoke")
    public void endToEndApiTest() {

        //Login
        Playwright playwright = Playwright.create();

        //Login Payload
        HashMap<String, Object> loginPayload = new HashMap<>();
        loginPayload.put("email", "naveenpathak715@gmail.com");
        loginPayload.put("password", "Sonu123@#");

        APIRequestContext apiRequest = playwright.request().newContext();
        APIResponse loginResponse = apiRequest.post("https://api.eventhub.rahulshettyacademy.com/api/auth/login",
                RequestOptions.create().setData(loginPayload));
        Assert.assertTrue(loginResponse.ok());
        System.out.println(loginResponse.text());
        String token = JsonPath.read(loginResponse.text(), "$.token");
        System.out.println("token: " + token);

        //Create Event
        HashMap<String, Object> eventPayload = new HashMap<>();
        eventPayload.put("title", "Playwright Test Event");
        eventPayload.put("description", "This is a playwright test event");
        eventPayload.put("category", "Workshop");
        eventPayload.put("venue", "Electronic City");
        eventPayload.put("city", "Bangalore");
        eventPayload.put("eventDate", "2027-10-30T09:00:00.000Z");
        eventPayload.put("price", "100");
        eventPayload.put("totalSeats", "500");

        APIResponse createEventResponse = apiRequest.post("https://api.eventhub.rahulshettyacademy.com/api/events",
                RequestOptions.create().setHeader("Authorization", "Bearer " + token).
                setHeader("Content-Type", "application/json").
                setData(eventPayload));
        Assert.assertTrue(createEventResponse.ok());
        System.out.println(createEventResponse.text());
        int eventId = JsonPath.read(createEventResponse.text(), "$.data.id");
        System.out.println("Event ID: " + eventId);

        //Get Event
        APIResponse getEventResponse = apiRequest.get("https://api.eventhub.rahulshettyacademy.com/api/events",
                RequestOptions.create().setQueryParam("page","1").
                setQueryParam("limit","12").
                setHeader("Authorization", "Bearer " + token));
        Assert.assertTrue(getEventResponse.ok());
        List<Integer> allEventIds = JsonPath.read(getEventResponse.text(), "$.data[*].id");
        Assert.assertTrue(allEventIds.contains(eventId), "Created event ID not found in the list of events");

        //Delete Event
        APIResponse deleteEventResponse = apiRequest.delete("https://api.eventhub.rahulshettyacademy.com/api/events/" + eventId,
                RequestOptions.create().setHeader("Authorization", "Bearer " + token));

        Assert.assertTrue(deleteEventResponse.ok());
        System.out.println("Event deleted successfully");

        //Verify deleted event does not exist in event list
        APIResponse getEventResponseAfterDeletion = apiRequest.get("https://api.eventhub.rahulshettyacademy.com/api/events",
                RequestOptions.create().setQueryParam("page","1").
                        setQueryParam("limit","12").
                        setHeader("Authorization", "Bearer " + token));
        Assert.assertTrue(getEventResponseAfterDeletion.ok());
        List<Integer> allEventIdsAfterDeletion = JsonPath.read(getEventResponseAfterDeletion.text(), "$.data[*].id");
        Assert.assertFalse(allEventIdsAfterDeletion.contains(eventId), "Created event ID still found in the list of events after deletion");
    }
}
