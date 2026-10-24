package aero.sita.iborders.tests;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import org.testng.annotations.BeforeMethod;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

public class TestBase {

    Playwright playwright;
    Browser browser;
    Page page;
    Properties properties;
    String browserName="";
    String envName="";
    String url="";
    String username="";
    String password="";


    @BeforeMethod(alwaysRun = true)
    public void setUp() throws IOException {
        PlaywrightAssertions.setDefaultAssertionTimeout(7000);
        properties = readProperties();
        //browserName = properties.getProperty("browser");
        username = properties.getProperty("username");
        password = properties.getProperty("password");

        playwright = Playwright.create();
        //mvn test -PRegression -Dbrowser=firefox
        browserName = System.getProperty("browser")!=null ?System.getProperty("browser") : properties.getProperty("browser");
        envName = System.getProperty("env")!=null ?System.getProperty("env") : properties.getProperty("env");
        browserName = browserName.trim().toLowerCase();
        envName = envName.trim().toLowerCase();
        if (browserName.equals("chrome")) {
            browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
        } else if (browserName.equals("firefox")) {
            browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(true));
        } else if (browserName.equals("safari")) {
            browser = playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(true));
        }

        url = properties.getProperty(envName + ".base_url");

        //browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        page = browser.newPage();
        page.setDefaultTimeout(8000);
    }

    public static Properties readProperties() throws IOException {
        Properties properties = new Properties();
        FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "/src/test/resources/config.properties");
        properties.load(fis);
        return properties;
    }
}
