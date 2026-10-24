package aero.sita.iborders.tests;

import aero.sita.iborders.pages.*;
import aero.sita.iborders.utils.DataProviderUtil;
import com.microsoft.playwright.Locator;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * The type Framework build test.
 */
public class FrameworkBuildDataDrivenTest extends TestBase{

    String eventName = "Rest Assured online Seminar";
    String name = "Naveen Pathak";
    String email = "naveenpathak715@gmail.com";
    String phoneNumber = "8073040700";
    static String bookingReferenceNumber = "";

    @Test(groups = "Framework", dataProvider = "eventBookingData", dataProviderClass = DataProviderUtil.class, description = "Create Event -Book that event and verify if it is booked")
    public void demoTest(HashMap<String, String> data) {

        LoginPage loginPage = new LoginPage(page, url, username, password);
        AdminEventsPage adminEventsPage = new AdminEventsPage(page);
        //Login
        DashboardPage dashboardPage = loginPage.loginToApplication();
        EventsPage eventsPage = new EventsPage(page);
        BookingPage bookingPage = new BookingPage(page);
        ViewBookingPage viewBookingPage = new ViewBookingPage(page);
        //Verify Dashboard page
        dashboardPage.waitForEventsToLoad();

        //Navigate Admin Events Page
        adminEventsPage.navigateToAdminEventsPage();
        // Fill all required details
        adminEventsPage.createEvent(
                data.get("titlePrefix"),
                data.get("description"),
                data.get("eventCategory"),
                data.get("city"),
                data.get("venue"),
                data.get("dateTime"),
                data.get("price"),
                data.get("totalSeats"));

        //Verify visibility of card which is added
        eventsPage.navigateToEventPage();
        Locator targetCard = eventsPage.verifyCreatedEvent(eventName);
        String seatCount = eventsPage.getSeatCount(eventName, targetCard);

        //Book the seat in created event
        eventsPage.clickBookNowButton();
        bookingReferenceNumber = bookingPage.fillBookingDetails(
                data.get("fullName"),
                data.get("email"),
                data.get("phone"));

        //Navigate to view Booking page
        bookingPage.navigateViewMyBookingPage();
        //Verify Booking created
        viewBookingPage.verifyBookingCreated(bookingReferenceNumber);
        eventsPage.navigateToEventPage();
        page.waitForTimeout(2000);
        String seatCountAfterBooking = eventsPage.getSeatCountAfterBooking(eventName);
        //Verify seat count reduced or not
        eventsPage.verifySeatCountReducedAfterBooking(seatCount, seatCountAfterBooking);
    }
}
