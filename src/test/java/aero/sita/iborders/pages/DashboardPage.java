package aero.sita.iborders.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class DashboardPage {

    Page page;

    public DashboardPage(Page page) {
        this.page = page;
    }

    public void waitForEventsToLoad() {
        //Navigate to create event page
        Locator browseEvents = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Browse Events →"));
        assertThat(browseEvents).isVisible();
    }

    public void createEvent(){

    }
}
