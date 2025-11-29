package automationexercises.pages.components;


import automationexercises.pages.*;
import automationexercises.utils.actions.PageActions;
import automationexercises.utils.dataReader.PropertyReader;
import automationexercises.utils.logs.LogsManager;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;

import static org.junit.Assert.assertThat;

public class NavigationBarComponent {

    private final Page page;
    PageActions pageActions ;

    public NavigationBarComponent(Page page) {
        this.page = page;
        this.pageActions= new PageActions(page);
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
        page.navigate(PropertyReader.getProperty("baseUrlWeb"));
        return this;
    }

    @Step("Click on Home button")
    public NavigationBarComponent clickHomeButton() {
        pageActions.find(homeButton).click();
        return this;
    }

    @Step("Click on Products button")
    public ProductsPage clickProductsButton() {
        pageActions.find(productsButton).click();
        return new ProductsPage(page);
    }

    @Step("Click on Cart button")
    public CartPage clickOnCartButton() {
        pageActions.find(cartButton).click();
        return new CartPage(page);
    }

    @Step("Click on Logout button")
    public SignupLoginPage clickOnLogoutButton() {
        pageActions.find(logoutButton).click();
        return new SignupLoginPage(page);
    }

    @Step("Click on Signup/Login Button")
    public SignupLoginPage clickOnSignupLoginButton() {
        pageActions.find(signupLoginButton).click();
        return new SignupLoginPage(page);
    }

    @Step("Click on Test Cases button")
    public TestCasesPage clickOnTestCasesButton() {
        pageActions.find(testCasesButton).click();
        return new TestCasesPage(page);
    }

    @Step("Click on Delete Account button")
    public DeleteAccountPage clickOnDeleteAccountButton() {
        pageActions.find(deleteAccountButton).click();
        return new DeleteAccountPage(page);
    }

    @Step("Click on Contact Us button")
    public ContactUsPage clickOnContactUsButton() {
        pageActions.find(contactUsButton).click();
        return new ContactUsPage(page);
    }

    // ================================
    // Validations
    // ================================

    @Step("Verify that user is logged out into Login Page")
    public SignupLoginPage verifyLogoutButtonNotVisible() {

        Assertions.assertTrue(pageActions.find(logoutButton).isHidden(), "The Logout button is unexpectedly visible, suggesting the user is still logged in.");
        return new SignupLoginPage(page);
    }

    @Step("Verify that Home Page is visible successfully")
    public NavigationBarComponent verifyHomePageVisible() {

        Assertions.assertTrue(pageActions.findByText(homePageLabel).isVisible(), "The Home Page label is not visible after navigation.");
        return this;
    }

    @Step("Verify that logged in as {username} is visible")
    public NavigationBarComponent verifyLoggedInUserName(String username) {

        String actualName = pageActions.find(userLabel).textContent();
        LogsManager.info("Verifying logged in user label. Actual: " + actualName + " | Expected: Logged in as " + username);
        Assertions.assertEquals(username, actualName,
                "Logged in user label does not match expected value. Expected: " + username + " | Actual: " + actualName);
        return this;
    }
}
