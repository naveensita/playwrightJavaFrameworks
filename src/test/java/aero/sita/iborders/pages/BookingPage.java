package aero.sita.iborders.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BookingPage {

    Page page;
    String bookingReferenceNumber;
    String bookingConfirmSuccessMsg = "Booking Confirmed!";

    private static final String FULL_NAME_PLACEHOLDER= "Your full name";
    private static final String EMAIL_TEST_ID= "customer-email";
    private static final String PHONE_NUMBER_CSS= "input[name='phone']";
    private static final String CONFIRM_BOOKING_CSS= "#confirm-booking";
    private static final String BOOKING_CONFIRMED_MESSAGE_CSS= ".text-xl";
    private static final String BOOKING_REFERENCE_CSS= ".booking-ref";
    private static final String VIEW_MY_BOOKINGS_BUTTON_NAME= "View My Bookings";
    private static final String MY_BOOKINGS_HEADER_CSS= ".text-3xl";


    public BookingPage(Page page) {
        this.page = page;
    }

    public String fillBookingDetails(String name, String email, String phoneNumber) {
        page.getByPlaceholder(FULL_NAME_PLACEHOLDER).fill(name);
        page.getByTestId(EMAIL_TEST_ID).fill(email);
        page.locator(PHONE_NUMBER_CSS).fill(phoneNumber);
        page.locator(CONFIRM_BOOKING_CSS).click();

        assertThat(page.locator(BOOKING_CONFIRMED_MESSAGE_CSS)).containsText(bookingConfirmSuccessMsg);
        bookingReferenceNumber = page.locator(BOOKING_REFERENCE_CSS).innerText();
        return bookingReferenceNumber;
    }

    public void navigateViewMyBookingPage(){
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(VIEW_MY_BOOKINGS_BUTTON_NAME)).click();
        assertThat(page.locator(MY_BOOKINGS_HEADER_CSS)).hasText("My Bookings");
    }
}
