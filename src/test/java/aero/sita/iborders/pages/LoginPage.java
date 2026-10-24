package aero.sita.iborders.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class LoginPage {

    Page page;
    String url;
    String username;
    String password;

    private static final String EMAIL_PLACEHOLDER = "you@email.com";
    private static final String PASSWORD_LABEL = "Password";

    public LoginPage(Page page, String url, String username, String password){
        this.page = page;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public DashboardPage loginToApplication(){
        page.navigate(url);
        page.getByPlaceholder(EMAIL_PLACEHOLDER).fill(username);
        page.getByLabel(PASSWORD_LABEL).fill(password);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();
        return new DashboardPage(page);
    }
}
