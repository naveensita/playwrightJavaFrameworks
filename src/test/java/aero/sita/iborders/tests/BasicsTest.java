package aero.sita.iborders.tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.LocatorAssertions;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Properties;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

/**
 * The type Basics test.
 */
public class BasicsTest {

    Playwright playwright;
    Browser browser;
    Page page;
    String eventName = "Rest Assured online Seminar";
    String name = "Naveen Pathak";
    String email = "naveenpathak715@gmail.com";
    String phoneNumber = "8073040700";
    static String bookingReferenceNumber = "";
    Properties properties;
    String browserName;
    String envName;
    String url;


    @BeforeMethod(alwaysRun = true)
    public void setUp() throws IOException {
        PlaywrightAssertions.setDefaultAssertionTimeout(7000);
        playwright = Playwright.create();
        properties = TestBase.readProperties();
        browserName = System.getProperty("browser")!=null ?System.getProperty("browser") : properties.getProperty("browser");
        envName = System.getProperty("env")!=null ?System.getProperty("env") : properties.getProperty("env");
        if (browserName.equals("chrome")) {
            browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        } else if (browserName.equals("firefox")) {
            browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false));
        } else if (browserName.equals("safari")) {
            browser = playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(false));
        }
        //browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        page = browser.newPage();
        page.setDefaultTimeout(8000);
        url = properties.getProperty(envName + ".base_url");
        page.navigate(url);
    }


    @Test(description = "Create Event -Book that event and verify if it is booked")
    public void demoTest() {

        System.out.println("Page title: " + page.title());
        //Verify Title of Login page
        assertThat(page).hasTitle("EventHub — Discover & Book Events");

        //Login
        //All actions default timeout is 10 seconds
        page.getByPlaceholder("you@email.com").fill("naveenpathak715@gmail.com");
        page.getByLabel("Password").fill("Sonu123@#");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();

        //Navigate to create event page
        Locator browseEvents = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Browse Events →"));
        assertThat(browseEvents).isVisible();

        // Fill all required details
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Admin")).click();
        page.locator("(//a[@href='/admin/events'])[1]").click();

        page.getByPlaceholder("Event title").fill(eventName, new Locator.FillOptions().setTimeout(10000));
        page.getByPlaceholder("Describe the event…").fill("Bengaluru Tech mega seminar");
        page.locator("#category").selectOption("Workshop");
        page.getByLabel("City").fill("Bangalore");
        page.getByPlaceholder("Venue name & address").fill("Electronic city");
        page.getByLabel("Event Date & Time").fill("2026-10-30T10:00");
        page.locator("//input[@id='price-($)']").fill("100");
        page.locator("#total-seats").fill("500");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("+ Add Event")).click();

        //Validate event created message
        assertThat(page.getByText("Event created!")).isVisible();//timeout default 5 seconds

        //Verify visibility of card which is added
        page.locator("#nav-events").click(new Locator.ClickOptions().setTimeout(12000));
        Locator eventCards = page.locator("#event-card");
        eventCards.first().waitFor();
        Locator targetCard = eventCards.filter(new Locator.FilterOptions().setHasText(eventName));
        assertThat(targetCard).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(10000));
        String seatCount = targetCard.getByText("seats").innerText();

        //Book the seat in created event
        targetCard.getByTestId("book-now-btn").click();
        page.getByPlaceholder("Your full name").fill(name);
        page.getByTestId("customer-email").fill(email);
        page.locator("input[name='phone']").fill(phoneNumber);
        page.locator("#confirm-booking").click();

        String bookingMessage = page.locator(".text-xl").innerText();
        assertThat(page.locator(".text-xl")).containsText("Booking Confirmed!");
        bookingReferenceNumber = page.locator(".booking-ref").innerText();

        //Verify Booking created
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("View My Bookings")).click();
        assertThat(page.locator(".text-3xl")).hasText("My Bookings");
        Locator bookingCards = page.getByTestId("booking-card");
        Locator targetBookingCard = bookingCards.filter(new Locator.FilterOptions().setHasText(bookingReferenceNumber));
        assertThat(targetBookingCard).isVisible();

        //Verify seat count reduced or not
        page.locator("#nav-events").click(new Locator.ClickOptions().setTimeout(12000));
        page.waitForTimeout(2000);
        Locator eventCardsAfterBooking = page.locator("#event-card");
        eventCards.first().waitFor();
        Locator targetCardAfterBooking = eventCardsAfterBooking.filter(new Locator.FilterOptions().setHasText(eventName));
        assertThat(targetCardAfterBooking).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(10000));
        String seatCountAfterBooking = targetCardAfterBooking.getByText("seats").innerText();
        int afterSeats = Integer.parseInt(seatCountAfterBooking.split(" ")[0]);
        int beforeSeats = Integer.parseInt(seatCount.split(" ")[0]);

        Assert.assertTrue(afterSeats < beforeSeats);

    }
}
