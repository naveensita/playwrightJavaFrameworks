package aero.sita.iborders.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class ViewBookingPage {

    Page page;
    private static final String BOOKING_CARD_TEST_ID = "booking-card";

    public ViewBookingPage(Page page) {
        this.page = page;
    }

    public void verifyBookingCreated(String bookingReferenceNumber){
        Locator bookingCards = page.getByTestId(BOOKING_CARD_TEST_ID);
        Locator targetBookingCard = bookingCards.filter(new Locator.FilterOptions().setHasText(bookingReferenceNumber));
        assertThat(targetBookingCard).isVisible();
    }
}
