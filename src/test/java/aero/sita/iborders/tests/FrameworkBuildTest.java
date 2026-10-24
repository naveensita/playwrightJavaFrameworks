package aero.sita.iborders.tests;

import aero.sita.iborders.utils.DataProviderUtil;
import aero.sita.iborders.pages.*;
import com.microsoft.playwright.*;
import org.testng.annotations.Test;

/**
 * The type Framework build test.
 */
public class FrameworkBuildTest extends TestBase{

    String eventName = "Rest Assured online Seminar";
    String name = "Naveen Pathak";
    String email = "naveenpathak715@gmail.com";
    String phoneNumber = "8073040700";
    static String bookingReferenceNumber = "";

    @Test(groups = "Framework", description = "Create Event -Book that event and verify if it is booked")
    public void demoTest() {

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
        adminEventsPage.createEvent(eventName, "Bengaluru Tech mega seminar",
                "Workshop", "Bangalore", "Electronic city",
                "2026-10-30T10:00", "100", "500");

        //Verify visibility of card which is added
        eventsPage.navigateToEventPage();
        Locator targetCard = eventsPage.verifyCreatedEvent(eventName);
        String seatCount = eventsPage.getSeatCount(eventName, targetCard);

        //Book the seat in created event
        eventsPage.clickBookNowButton();
        bookingReferenceNumber = bookingPage.fillBookingDetails(name, email, phoneNumber);

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
