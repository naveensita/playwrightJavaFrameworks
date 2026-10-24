package aero.sita.iborders.tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;


public class UiValidationContinuedTest {

    Playwright playwright;
    Browser browser;
    BrowserContext context;
    Page page;
    String browserName;
    Properties properties;

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
        page.navigate("https://rahulshettyacademy.com/AutomationPractice/");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(){
        context.tracing().stop(new Tracing.StopOptions().setPath(Paths.get("trace.zip")));
    }

    @Test(priority=1, groups = "Smoke")
    public void popUpValidations(){
        assertThat(page.getByPlaceholder("Hide/Show Example")).isVisible();
        page.locator("#hide-textbox").click();
        assertThat(page.getByPlaceholder("Hide/Show Example")).isHidden();
        page.onDialog(dialog->dialog.accept());
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Alert")).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Mouse Hover")).hover();
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Top")).click();
        FrameLocator framePage = page.frameLocator("#courses-iframe");
        framePage.getByRole(AriaRole.LINK, new FrameLocator.GetByRoleOptions().setName("Learning paths")).click();
        String Text = framePage.locator(".inner-box h1").textContent();
        System.out.println("Text : "+Text);
    }


    @Test(priority=2, groups = "Smoke")
    public void screenshotTest(){
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("pagescreenshot.png")));
        Locator displayedEditBox = page.getByPlaceholder("Hide/Show Example");
        displayedEditBox.screenshot(new Locator.ScreenshotOptions().setPath(Paths.get("editboxscreenshot.png")));
        page.locator("#hide-textbox").click();
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("pagepostscreenshot.png")));
    }
}
