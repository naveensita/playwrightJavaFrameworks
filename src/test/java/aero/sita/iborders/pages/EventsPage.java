package aero.sita.iborders.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.LocatorAssertions;
import org.testng.Assert;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class EventsPage {

    Page page;
    String seatCount;
    Locator eventCards;
    Locator targetCard;

    private static final String Events_CSS = "#nav-events";
    private static final String EVENT_CARDS_CSS = "#event-card";
    private static final String BOOK_NOW_BTN_TEST_ID = "book-now-btn";

    public EventsPage(Page page) {
        this.page = page;
    }

    public void navigateToEventPage() {
        page.locator(Events_CSS).click(new Locator.ClickOptions().setTimeout(12000));
    }

    public Locator waitForEventCardsToLoad() {
        eventCards = page.locator(EVENT_CARDS_CSS);
        eventCards.first().waitFor();
        assertThat(eventCards.first()).isVisible();
        return eventCards;
    }

    public Locator verifyCreatedEvent(String eventName){
        eventCards = waitForEventCardsToLoad();
        targetCard = eventCards.filter(new Locator.FilterOptions().setHasText(eventName));
        assertThat(targetCard).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(10000));
        return targetCard;
    }

    public String getSeatCount(String eventName, Locator targetCard){
        eventCards.first().waitFor();
        seatCount = targetCard.getByText("seats").innerText();
        return seatCount;
    }

    public String getSeatCountAfterBooking(String eventName){
        Locator eventCardsAfterBooking = page.locator(EVENT_CARDS_CSS);
        eventCardsAfterBooking.first().waitFor();
        Locator targetCardAfterBooking = eventCardsAfterBooking.filter(new Locator.FilterOptions().setHasText(eventName));
        assertThat(targetCardAfterBooking).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(10000));
        return targetCardAfterBooking.getByText("seats").innerText();
    }

    public void clickBookNowButton(){
        targetCard.getByTestId(BOOK_NOW_BTN_TEST_ID).click();
    }

    public void verifySeatCountReducedAfterBooking(String seatCount, String seatCountAfterBooking){
        //Verify seat count reduced or not
        int beforeSeats = Integer.parseInt(seatCount.split(" ")[0]);
        int afterSeats = Integer.parseInt(seatCountAfterBooking.split(" ")[0]);
        Assert.assertTrue(afterSeats < beforeSeats);
    }
}
