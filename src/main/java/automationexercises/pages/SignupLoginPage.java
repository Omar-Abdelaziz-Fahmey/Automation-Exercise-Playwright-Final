package automationexercises.pages;

import automationexercises.pages.components.NavigationBarComponent;
import automationexercises.utils.dataReader.PropertyReader;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;

public class SignupLoginPage {

    private final Page page;
    private final String signupLoginEndpoint = "/login";
    public NavigationBarComponent navigationBar;

    public SignupLoginPage(Page page) {
        this.page = page;
        navigationBar = new NavigationBarComponent(page);
    }

    // locators
    // private final By loginEmail = By.cssSelector("[data-qa=\"login-email\"]");
    // private final By loginPassword =
    // By.cssSelector("[data-qa=\"login-password\"]");
    // private final By loginButton = By.cssSelector("[data-qa=\"login-button\"]");
    // private final By signupName = By.cssSelector("[data-qa=\"signup-name\"]");
    // private final By signupEmail = By.cssSelector("[data-qa=\"signup-email\"]");
    // private final By signupButton =
    // By.cssSelector("[data-qa=\"signup-button\"]");
    //
    // private final By signupLabel = By.cssSelector(".signup-form > h2");
    // private final By loginError = By.cssSelector(".login-form p");
    // private final By registerError = By.cssSelector(".signup-form p");

    private String loginEmail = "[data-qa=\"login-email\"]";
    private String loginPassword = "Password";
    private String loginButton = "[data-qa=\"login-button\"]";
    private String signupName = "Name";
    private String signupEmail = "[data-qa=\"signup-email\"]";
    private String signupButton = "[data-qa=\"signup-button\"]";

    private String signupLabel = ".signup-form > h2";
    private String loginError = "Your email or password is incorrect!";
    private String registerError = "Email Address already exist!";

    // actions
    @Step("Navigate to Signup/Login page")
    public SignupLoginPage navigate() {
        page.navigate(PropertyReader.getProperty("baseUrlWeb") + signupLoginEndpoint);
        return this;
    }

    @Step("Enter login email: {email}")
    public SignupLoginPage enterLoginEmail(String email) {
        page.locator(loginEmail).fill(email);
        return this;
    }

    @Step("Enter login password: {password}")
    public SignupLoginPage enterLoginPassword(String password) {
        page.getByPlaceholder(loginPassword).fill(password);
        return this;
    }

    @Step("Click on login button")
    public SignupLoginPage clickLoginButton() {
        page.locator(loginButton).click();
        return this;
    }

    @Step("Enter signup name: {name}")
    public SignupLoginPage enterSignupName(String name) {
        page.getByPlaceholder(signupName).fill(name);
        return this;
    }

    @Step("Enter signup email: {email}")
    public SignupLoginPage enterSignupEmail(String email) {
        page.locator(signupEmail).fill(email);
        return this;
    }

    @Step("Click on signup button")
    public SignupLoginPage clickSignupButton() {
        page.locator(signupButton).click();
        return this;
    }

    // validations
    @Step("Verify signup label visible")
    public SignupLoginPage verifySignupLabelVisible() {
        Assertions.assertEquals("New User Signup!", page.locator(signupLabel).innerText(),
                "Signup label is not visible");
        return this;
    }

    @Step("Verify login error message: {expectedMessage}")
    public SignupLoginPage verifyLoginErrorMessage(String expectedMessage) {
        String actualMessage = page.getByText(loginError).innerText();
        Assertions.assertEquals(expectedMessage, actualMessage, "Login error message does not match");
        return this;
    }

    @Step("Verify register error message: {expectedMessage}")
    public SignupLoginPage verifyRegisterErrorMessage(String expectedMessage) {
        String actualMessage = page.getByText(registerError).innerText();
        Assertions.assertEquals(expectedMessage, actualMessage, "Register error message does not match");
        return this;
    }

}
