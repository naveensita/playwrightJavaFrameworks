package aero.sita.iborders.tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class MockWebTest {

    Playwright playwright;
    Browser browser;
    BrowserContext context;
    Page page;
    Properties properties;
    String browserName;
    String envName;
    String url;

    @Test(description = "Sandbox banner is shown when 6 events are returned")
    public void mockingResponseWebTest() throws IOException {

        playwright = Playwright.create();
        properties = TestBase.readProperties();
        browserName = System.getProperty("browser")!=null ?System.getProperty("browser") : properties.getProperty("browser");
        envName = System.getProperty("env")!=null ?System.getProperty("env") : properties.getProperty("env");
        url = properties.getProperty(envName + ".base_url");
        if (browserName.equals("chrome")) {
            browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        } else if (browserName.equals("firefox")) {
            browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false));
        } else if (browserName.equals("safari")) {
            browser = playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(false));
        }
        //browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        context = browser.newContext();
        page = context.newPage();
        page.navigate(url);

        page.getByPlaceholder("you@email.com").fill("naveenpathak715@gmail.com");
        page.getByLabel("Password").fill("Sonu123@#");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();

        page.waitForLoadState();
        //Navigate to create event page
        Locator browseEvents = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Browse Events →"));
        assertThat(browseEvents).isVisible();
        //Mocking the response
        page.route("**/api/events**", route -> route.fulfill(
                new Route.FulfillOptions().setPath(Paths.get("src/test/resources/events_6.json"))
        ));
        page.navigate("https://eventhub.rahulshettyacademy.com/events");

        Locator eventCards = page.getByTestId("event-card");
        assertThat(eventCards.first()).isVisible();
        int count = eventCards.count();
        Assert.assertEquals(count, 6);
        assertThat(page.locator(".mx-1").first()).isVisible();

        //Mocking the response
        page.route("**/api/events**", route -> route.fulfill(
                new Route.FulfillOptions().setPath(Paths.get("src/test/resources/events_4.json"))
        ));
        page.navigate("https://eventhub.rahulshettyacademy.com/events");

        Locator eventCards1 = page.getByTestId("event-card");
        assertThat(eventCards1.first()).isVisible();
        int count1 = eventCards1.count();
        Assert.assertEquals(count1, 4);
        assertThat(page.locator(".mx-1").first()).isHidden();
    }

    @Test(description = "Validate if it is not allowing to show another account booking")
    public void mockingRequestWebTest() throws IOException {

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
        context = browser.newContext();
        page = context.newPage();
        url = properties.getProperty(envName + ".base_url");
        page.navigate(url);

        page.getByPlaceholder("you@email.com").fill("naveenpathak715@gmail.com");
        page.getByLabel("Password").fill("Sonu123@#");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();

        page.waitForLoadState();
        //Navigate to create event page
        Locator browseEvents = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Browse Events →"));
        assertThat(browseEvents).isVisible();

        //Mocking the request
        page.getByTestId("nav-bookings").click();
        // 2. Wait until the "View Details" button is actually present in the DOM
        Locator viewDetailsBtn = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("View Details")).first();
        viewDetailsBtn.waitFor();

        page.route("**/api/bookings**", route -> route.resume(
                new Route.ResumeOptions().setUrl("https://eventhub.rahulshettyacademy.com/bookings/77696")
        ));

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("View Details")).first().click();
        assertThat(page.getByText("Booking not found")).isVisible();
    }
}