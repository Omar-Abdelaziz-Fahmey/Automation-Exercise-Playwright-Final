package automationexercises.pages.components;

import automationexercises.pages.*;
import automationexercises.utils.dataReader.PropertyReader;
import automationexercises.utils.logs.LogsManager;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitUntilState;
import io.qameta.allure.Step;
import org.testng.Assert;

public class NavigationBarComponent {

    private final Page page;

    public NavigationBarComponent(Page page) {
        this.page = page;
    }

    // Locators (Playwright)
    private final String homeButton = "//a[.=' Home']";
    private final String productsButton = "a[href='/products']";
    private final String cartButton = "//a[.=' Cart']";
    private final String logoutButton = "//a[.=' Logout']";
    private final String signupLoginButton = "//a[.=' Signup / Login']";
    private final String testCasesButton = "//a[.=' Test Cases']";
    private final String deleteAccountButton = "//a[.=' Delete Account']";
    private final String apiButton = "//a[.=' API Testing']";
    private final String videoTutorials = "//a[.=' Video Tutorials']";
    private final String contactUsButton = "//a[.=' Contact us']";
    private final String homePageLabel = "FEATURES ITEMS";
    private final String userLabel = "b";

    // Utility: Get Locator quickly

    // ================================
    // Actions
    // ================================

    @Step("Navigate to the web application")
    public NavigationBarComponent navigate() {
        page.navigate(PropertyReader.getProperty("baseUrlWeb"),
                new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        return this;
    }

    @Step("Click on Home button")
    public NavigationBarComponent clickHomeButton() {
        page.locator(homeButton).click();
        return this;
    }

    @Step("Click on Products button")
    public ProductsPage clickProductsButton() {
        page.locator(productsButton).click();
        return new ProductsPage(page);
    }

    @Step("Click on Cart button")
    public CartPage clickOnCartButton() {
        page.locator(cartButton).click();
        return new CartPage(page);
    }

    @Step("Click on Logout button")
    public SignupLoginPage clickOnLogoutButton() {
        page.locator(logoutButton).click();
        return new SignupLoginPage(page);
    }

    @Step("Click on Signup/Login Button")
    public SignupLoginPage clickOnSignupLoginButton() {
        page.locator(signupLoginButton).click();
        return new SignupLoginPage(page);
    }

    @Step("Click on Test Cases button")
    public TestCasesPage clickOnTestCasesButton() {
        page.locator(testCasesButton).click();
        return new TestCasesPage(page);
    }

    @Step("Click on Delete Account button")
    public DeleteAccountPage clickOnDeleteAccountButton() {
        page.locator(deleteAccountButton).click();
        return new DeleteAccountPage(page);
    }

    @Step("Click on Contact Us button")
    public ContactUsPage clickOnContactUsButton() {
        page.locator(contactUsButton).click();
        return new ContactUsPage(page);
    }

    // ================================
    // Validations
    // ================================

    @Step("Verify that user is logged out into Login Page")
    public SignupLoginPage verifyLogoutButtonNotVisible() {

        Assert.assertTrue(page.locator(logoutButton).isHidden(),
                "The Logout button is unexpectedly visible, suggesting the user is still logged in.");
        return new SignupLoginPage(page);
    }

    @Step("Verify that Home Page is visible successfully")
    public NavigationBarComponent verifyHomePageVisible() {

        Assert.assertTrue(page.getByText(homePageLabel).isVisible(),
                "The Home Page label is not visible after navigation.");
        return this;
    }

    @Step("Verify that logged in as {username} is visible")
    public NavigationBarComponent verifyLoggedInUserName(String username) {

        String actualName = page.locator(userLabel).textContent();
        LogsManager.info(
                "Verifying logged in user label. Actual: " + actualName + " | Expected: Logged in as " + username);
        Assert.assertEquals(actualName, username,
                "Logged in user label does not match expected value. Expected: " + username + " | Actual: "
                        + actualName);
        return this;
    }
}
