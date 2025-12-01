package automationexercises.pages;

import automationexercises.pages.components.NavigationBarComponent;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

public class SignupPage {

    private final Page page;
    public NavigationBarComponent navigationBar;
    private static SoftAssert softAssert = new SoftAssert();

    public SignupPage(Page page) {
        this.page = page;
        this.navigationBar = new NavigationBarComponent(page);
    }

    // Locators (Strings like your new style)
    private String mrTitle = "#uniform-id_gender1";
    private String mrsTitle = "#uniform-id_gender2";
    private String name = "[data-qa=\"name\"]";
    private String email = "[data-qa=\"email\"]";
    private String password = "#password";
    private String day = "#days";
    private String month = "#months";
    private String year = "#years";
    private String newsletter = "#newsletter";
    private String specialOffers = "#optin";

    // Address info
    private String firstName = "#first_name";
    private String lastName = "#last_name";
    private String company = "#company";
    private String address1 = "#address1";
    private String address2 = "#address2";
    private String country = "#country";
    private String state = "#state";
    private String city = "#city";
    private String zipcode = "#zipcode";
    private String mobileNumber = "#mobile_number";

    private String createAccountButton = "[data-qa=\"create-account\"]";
    private String accountCreatedLabel = "p";
    private String continueButton = "[data-qa=\"continue-button\"]";

    // Actions
    @Step("Choose title: {title}")
    public SignupPage chooseTitle(String title) {
        switch (title.toLowerCase()) {
            case "mr":
                page.locator(mrTitle).click();
                break;
            case "mrs":
                page.locator(mrsTitle).click();
                break;
            default:
                throw new IllegalArgumentException("Invalid title: " + title);
        }
        return this;
    }

    @Step("Enter name: {userName}")
    public SignupPage enterName(String userName) {
        page.locator(name).fill(userName);
        return this;
    }

    @Step("Enter email: {userEmail}")
    public SignupPage enterEmail(String userEmail) {
        page.locator(email).fill(userEmail);
        return this;
    }

    @Step("Enter password: {userPassword}")
    public SignupPage enterPassword(String userPassword) {
        page.locator(password).fill(userPassword);
        return this;
    }

    @Step("Select date of birth: {day}-{month}-{year}")
    public SignupPage selectDateOfBirth(String day, String month, String year) {
        page.locator(this.day).selectOption(day);
        page.locator(this.month).selectOption(month);
        page.locator(this.year).selectOption(year);
        return this;
    }

    @Step("Subscribe to newsletter")
    public SignupPage subscribeToNewsletter() {
        page.locator(newsletter).check();
        return this;
    }

    @Step("Receive special offers")
    public SignupPage receiveSpecialOffers() {
        page.locator(specialOffers).check();
        return this;
    }

    @Step("Enter first name: {firstName}")
    public SignupPage enterFirstName(String firstName) {
        page.locator(this.firstName).fill(firstName);
        return this;
    }

    @Step("Enter last name: {lastName}")
    public SignupPage enterLastName(String lastName) {
        page.locator(this.lastName).fill(lastName);
        return this;
    }

    @Step("Enter company: {company}")
    public SignupPage enterCompany(String company) {
        page.locator(this.company).fill(company);
        return this;
    }

    @Step("Enter address1: {address1}")
    public SignupPage enterAddress1(String address1) {
        page.locator(this.address1).fill(address1);
        return this;
    }

    @Step("Enter address2: {address2}")
    public SignupPage enterAddress2(String address2) {
        page.locator(this.address2).fill(address2);
        return this;
    }

    @Step("Select country: {country}")
    public SignupPage selectCountry(String country) {
        page.locator(this.country).selectOption(country);
        return this;
    }

    @Step("Enter state: {state}")
    public SignupPage enterState(String state) {
        page.locator(this.state).fill(state);
        return this;
    }

    @Step("Enter city: {city}")
    public SignupPage enterCity(String city) {
        page.locator(this.city).fill(city);
        return this;
    }

    @Step("Enter zipcode: {zipcode}")
    public SignupPage enterZipcode(String zipcode) {
        page.locator(this.zipcode).fill(zipcode);
        return this;
    }

    @Step("Enter mobile number: {mobileNumber}")
    public SignupPage enterMobileNumber(String mobileNumber) {
        page.locator(this.mobileNumber).fill(mobileNumber);
        return this;
    }

    @Step("Click on Create Account button")
    public SignupPage clickCreateAccountButton() {
        page.locator(createAccountButton).click();
        return this;
    }

    @Step("Click on Continue button")
    public NavigationBarComponent clickContinueButton() {
        page.locator(continueButton).click();
        return new NavigationBarComponent(page);
    }

    // Validations
    @Step("Verify account created label is visible")
    public SignupPage verifyAccountCreated() {
        Assert.assertTrue(
                page.locator(accountCreatedLabel).nth(0).isVisible(),
                "Account Created label is NOT visible");
        return this;
    }

    @Step("Verify user is on signup page")
    public SignupPage verifyOnSignupPage() {
        Assert.assertTrue(
                page.locator(createAccountButton).isVisible(),
                "User is NOT on signup page");
        return this;
    }
}
