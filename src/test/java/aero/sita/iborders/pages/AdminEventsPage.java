package aero.sita.iborders.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class AdminEventsPage {

    Page page;
    private static final String ADMIN_EVENTS_LINK = "(//a[@href='/admin/events'])[1]";
    private static final String EVENT_NAME_PLACEHOLDER = "Event title";
    private static final String EVENT_DESCRIPTION_PLACEHOLDER = "Describe the event…";
    private static final String EVENT_CATEGORY_CSS = "#category";
    private static final String CITY_LABEL = "City";
    private static final String NAME_ADDRESS_PLACEHOLDER = "Venue name & address";
    private static final String EVENT_DATE_TIME_PLACEHOLDER = "Event Date & Time";
    private static final String PRICE_XPATH = "//input[@id='price-($)']";
    private static final String TOTAL_SEATS_CSS = "#total-seats";
    private static final String ADD_EVENT_BUTTON_NAME = "+ Add Event";
    private static final String EVENT_CREATION_SUCCESS_MSG = "Event created!";

    public AdminEventsPage(Page page) {
        this.page = page;
    }

    public void navigateToAdminEventsPage(){
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Admin")).click();
        page.locator(ADMIN_EVENTS_LINK).click();
    }

    public void createEvent(String eventName, String eventDescription, String eventCategory,
                            String city, String address, String dateAndTime, String price, String totalSeats) {
        page.getByPlaceholder(EVENT_NAME_PLACEHOLDER).fill(eventName, new Locator.FillOptions().setTimeout(10000));
        page.getByPlaceholder(EVENT_DESCRIPTION_PLACEHOLDER).fill(eventDescription);
        page.locator(EVENT_CATEGORY_CSS).selectOption(eventCategory);
        page.getByLabel(CITY_LABEL).fill(city);
        page.getByPlaceholder(NAME_ADDRESS_PLACEHOLDER).fill(address);
        page.getByLabel(EVENT_DATE_TIME_PLACEHOLDER).fill(dateAndTime);
        page.locator(PRICE_XPATH).fill(price);
        page.locator(TOTAL_SEATS_CSS).fill(totalSeats);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(ADD_EVENT_BUTTON_NAME)).click();

        //Validate event created message
        assertThat(page.getByText(EVENT_CREATION_SUCCESS_MSG)).isVisible();//timeout default 5 seconds
    }
}
