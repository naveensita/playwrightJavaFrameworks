package aero.sita.iborders.tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class MoreUiValidationTest {

    Playwright playwright;
    Browser browser;
    BrowserContext context;
    Page page;
    private static String username = "";
    String password = "Learning@830$3mK2";
    String userCategory = "Consultant";
    Properties properties;
    String browserName;

    @BeforeMethod(alwaysRun = true)
    public void setUp() throws IOException {
        playwright = Playwright.create();
        properties = TestBase.readProperties();
        browserName = System.getProperty("browser")!=null ?System.getProperty("browser") : properties.getProperty("browser");
        if (browserName.equals("chrome")) {
            browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        } else if (browserName.equals("firefox")) {
            browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false));
        } else if (browserName.equals("safari")) {
            browser = playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(false));
        }
        //browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        context = browser.newContext();
        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));
        page = context.newPage();

        page.setDefaultTimeout(10000);
        PlaywrightAssertions.setDefaultAssertionTimeout(5000);
        page.navigate("https://rahulshettyacademy.com/loginpagePractise/");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(){
        context.tracing().stop(new Tracing.StopOptions().setPath(Paths.get("trace.zip")));
    }

    @Test(priority=1, groups = "Smoke")
    public void windowHandlingTest(){

        String title = page.title();
        assertThat(page).hasTitle(title);
        Page newPage = context.waitForPage(()->page.locator(".blinkingText").first().click());
        newPage.waitForLoadState();
        String childText = newPage.locator(".red").textContent();
        String email = childText.split(" at ")[1].split(" ")[0];
        username = email.split("@")[1].split("\\.")[0];
        System.out.println("username : "+username);

        page.getByLabel("Username:").fill(username);
        page.getByLabel("Password:").fill(password);
        page.locator("input[value='admin']").click();
        page.locator("select.form-control").selectOption(userCategory);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();
        page.waitForLoadState();
        page.waitForTimeout(3000);
        // Close child tab so it doesn't linger
        newPage.close();
    }

    @Test(priority=2, dependsOnMethods = "windowHandlingTest")
    public void uiControlsTest(){

        page.getByLabel("Username:").fill(username);
        page.getByLabel("Password:").fill(password);

        //Radio button
        Locator userRadioBtn = page.getByRole(AriaRole.RADIO, new Page.GetByRoleOptions().setName("User"));
        userRadioBtn.click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Okay")).click();
        Assert.assertTrue(userRadioBtn.isChecked());

        //Select box or dropdown
        page.getByRole(AriaRole.COMBOBOX).selectOption(userCategory);

        //Checkbox
        Locator termsCheckBox = page.getByRole(AriaRole.CHECKBOX, new Page.GetByRoleOptions().
                setName("I Agree to the terms and conditions"));
        termsCheckBox.check();
        Assert.assertTrue(termsCheckBox.isChecked());

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();
        page.waitForLoadState();
        page.waitForTimeout(3000);
    }
}
