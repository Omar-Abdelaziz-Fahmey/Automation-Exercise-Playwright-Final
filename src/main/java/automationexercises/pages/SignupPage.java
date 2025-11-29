package automationexercises.pages;

import automationexercises.pages.components.NavigationBarComponent;
import automationexercises.utils.actions.PageActions;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;

public class SignupPage {

    private final Page page;
    private final PageActions pageActions;
    public NavigationBarComponent navigationBar;

    public SignupPage(Page page) {
        this.page = page;
        this.pageActions = new PageActions(page);
        this.navigationBar = new NavigationBarComponent(page);
    }

    // Locators (Strings like your new style)
    private String mrTitle     = "#uniform-id_gender1";
    private String mrsTitle    = "#uniform-id_gender2";
    private String name        = "[data-qa=\"name\"]";
    private String email       = "[data-qa=\"email\"]";
    private String password    = "#password";
    private String day         = "#days";
    private String month       = "#months";
    private String year        = "#years";
    private String newsletter  = "#newsletter";
    private String specialOffers = "#optin";

    // Address info
    private String firstName   = "#first_name";
    private String lastName    = "#last_name";
    private String company     = "#company";
    private String address1    = "#address1";
    private String address2    = "#address2";
    private String country     = "#country";
    private String state       = "#state";
    private String city        = "#city";
    private String zipcode     = "#zipcode";
    private String mobileNumber = "#mobile_number";

    private String createAccountButton = "[data-qa=\"create-account\"]";
    private String accountCreatedLabel = "p";
    private String continueButton      = "[data-qa=\"continue-button\"]";


    // Actions
    @Step("Choose title: {title}")
    public SignupPage chooseTitle(String title) {
        switch (title.toLowerCase()) {
            case "mr":
                pageActions.find(mrTitle).click();
                break;
            case "mrs":
                pageActions.find(mrsTitle).click();
                break;
            default:
                throw new IllegalArgumentException("Invalid title: " + title);
        }
        return this;
    }

    @Step("Enter name: {userName}")
    public SignupPage enterName(String userName) {
        pageActions.find(name).fill(userName);
        return this;
    }

    @Step("Enter email: {userEmail}")
    public SignupPage enterEmail(String userEmail) {
        pageActions.find(email).fill(userEmail);
        return this;
    }

    @Step("Enter password: {userPassword}")
    public SignupPage enterPassword(String userPassword) {
        pageActions.find(password).fill(userPassword);
        return this;
    }

    @Step("Select date of birth: {day}-{month}-{year}")
    public SignupPage selectDateOfBirth(String day, String month, String year) {
        pageActions.find(this.day).selectOption(day);
        pageActions.find(this.month).selectOption(month);
        pageActions.find(this.year).selectOption(year);
        return this;
    }

    @Step("Subscribe to newsletter")
    public SignupPage subscribeToNewsletter() {
        pageActions.find(newsletter).check();
        return this;
    }

    @Step("Receive special offers")
    public SignupPage receiveSpecialOffers() {
        pageActions.find(specialOffers).check();
        return this;
    }

    @Step("Enter first name: {firstName}")
    public SignupPage enterFirstName(String firstName) {
        pageActions.find(this.firstName).fill(firstName);
        return this;
    }

    @Step("Enter last name: {lastName}")
    public SignupPage enterLastName(String lastName) {
        pageActions.find(this.lastName).fill(lastName);
        return this;
    }

    @Step("Enter company: {company}")
    public SignupPage enterCompany(String company) {
        pageActions.find(this.company).fill(company);
        return this;
    }

    @Step("Enter address1: {address1}")
    public SignupPage enterAddress1(String address1) {
        pageActions.find(this.address1).fill(address1);
        return this;
    }

    @Step("Enter address2: {address2}")
    public SignupPage enterAddress2(String address2) {
        pageActions.find(this.address2).fill(address2);
        return this;
    }

    @Step("Select country: {country}")
    public SignupPage selectCountry(String country) {
        pageActions.find(this.country).selectOption(country);
        return this;
    }

    @Step("Enter state: {state}")
    public SignupPage enterState(String state) {
        pageActions.find(this.state).fill(state);
        return this;
    }

    @Step("Enter city: {city}")
    public SignupPage enterCity(String city) {
        pageActions.find(this.city).fill(city);
        return this;
    }

    @Step("Enter zipcode: {zipcode}")
    public SignupPage enterZipcode(String zipcode) {
        pageActions.find(this.zipcode).fill(zipcode);
        return this;
    }

    @Step("Enter mobile number: {mobileNumber}")
    public SignupPage enterMobileNumber(String mobileNumber) {
        pageActions.find(this.mobileNumber).fill(mobileNumber);
        return this;
    }

    @Step("Click on Create Account button")
    public SignupPage clickCreateAccountButton() {
        pageActions.find(createAccountButton).click();
        return this;
    }

    @Step("Click on Continue button")
    public NavigationBarComponent clickContinueButton() {
        pageActions.find(continueButton).click();
        return new NavigationBarComponent(page);
    }


    // Validations
    @Step("Verify account created label is visible")
    public SignupPage verifyAccountCreated() {
        Assertions.assertTrue(
                pageActions.find(accountCreatedLabel).nth(0).isVisible(),
                "Account Created label is NOT visible"
        );
        return this;
    }

    @Step("Verify user is on signup page")
    public SignupPage verifyOnSignupPage() {
        Assertions.assertTrue(
                pageActions.find(createAccountButton).isVisible(),
                "User is NOT on signup page"
        );
        return this;
    }
}
