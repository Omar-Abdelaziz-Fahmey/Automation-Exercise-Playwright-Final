package automationexercises.pages;

import automationexercises.pages.components.NavigationBarComponent;
import automationexercises.utils.dataReader.PropertyReader;
import automationexercises.utils.logs.LogsManager;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitUntilState;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import org.testng.Assert;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ContactUsPage {

    private final Page page;
    NavigationBarComponent navigationBar;

    public ContactUsPage(Page page) {
        this.page = page;
        navigationBar = new NavigationBarComponent(page);

    }

    // variables
    private final String contactUsEndpoint = "/contact_us";

    // locators
    String contactUsText = ".col-sm-12>.title";
    String nameInput = "*[name=name]";
    String emailInput = "*[name=email]";
    String subjectInput = "*[name=subject]";
    String messageInput = "*[name=message]";
    String chooseFileButton = "*[name=upload_file]";
    String submitButton = "*[name=submit]";
    String successMessage = ".status.alert.alert-success";

    // actions
    @Step("Click on Contact Us button from navigation bar")
    public ContactUsPage clickOnContactUs() {
        navigationBar.clickOnContactUsButton();
        return this;
    }

    @Step("Navigate to Contact Us page")
    public ContactUsPage navigate() {
        page.navigate(PropertyReader.getProperty("baseUrlWeb") + contactUsEndpoint,
                new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        return this;
    }

    @Step("Fill Contact Us form with Name: {name}, Email: {email}, Subject: {subject}, Message: {message}")
    public ContactUsPage fillContactUsForm(String name, String email, String subject, String message) {
        page.locator(nameInput).fill(name);
        page.locator(emailInput).fill(email);
        page.locator(subjectInput).fill(subject);
        page.locator(messageInput).fill(message);
        return this;
    }

    @Step("Fill Contact Us form with Name: {name}, Email: {email}, Subject: {subject}, Message: {message} and upload file: {filePath}")
    public ContactUsPage fillContactUsForm(String name, String email, String subject, String message, String filePath) {

        page.locator(nameInput).fill(name);
        page.locator(emailInput).fill(email);
        page.locator(subjectInput).fill(subject);
        page.locator(messageInput).fill(message);
        page.locator(chooseFileButton).click();
        String fileAbsolute = System.getProperty("user.dir") + File.separator + filePath;
        page.setInputFiles(chooseFileButton, Paths.get(fileAbsolute));
        return this;
    }

    @Step("Click Submit Button")
    public ContactUsPage clickSubmitButton() {
        page.locator(submitButton).click();
        return this;
    }

    @Step("Accept Alert")
    public ContactUsPage acceptAlert() {
        page.onceDialog(dialog -> dialog.accept());
        return this;
    }

    // validations
    @Step("Verify Contact Us page is displayed")
    public ContactUsPage verifyContactUsPageIsDisplayed() {
        LogsManager.info("Verifying Contact Us page is displayed");
        Assertions.assertTrue(page.locator(contactUsText).isVisible());
        return this;
    }

    @Step("Verify alert message after submit: {expectedMessage}")
    public ContactUsPage verifyAlertMessageAfterSubmit(String expectedMessage) {
        page.onceDialog(dialog -> {
            String actualAlertText = dialog.message();
            LogsManager.info("Verifying alert message after submit actual: " + actualAlertText + " and expected: "
                    + expectedMessage);
            Assertions.assertEquals(expectedMessage, actualAlertText, "Alert message is not matched");
            dialog.accept();
        });
        return this;
    }

    @Step("Verify success message after submit: {expectedMessage}")
    public ContactUsPage verifySuccessMessageAfterSubmit(String expectedMessage) {
        String actualMessage = page.locator(successMessage).innerText();
        LogsManager.info("Verifying success message after submit actual: " + actualMessage + " and expected :"
                + expectedMessage);
        Assertions.assertEquals(expectedMessage, actualMessage, "Success message is not matched");
        return this;
    }

    @Step("Verify success message is not visible")
    public ContactUsPage verifySuccessMessageIsNotVisible() {
        LogsManager.info("Verifying success message is not visible");
        Assertions.assertFalse(page.locator(successMessage).isVisible());
        return this;
    }

    @Step("Verify Name field validation message")
    public ContactUsPage verifyNameFieldValidationMessage() {
        String validationMessage = (String) page.locator(nameInput).evaluate("element => element.validationMessage");
        Assertions.assertEquals("Please fill out this field.", validationMessage, "Validation message is not matched");
        return this;
    }

    @Step("Verify Email field validation message")
    public ContactUsPage verifyEmailFieldValidationMessage() {
        String validationMessage = (String) page.locator(emailInput).evaluate("element => element.validationMessage");
        Assertions.assertEquals("Please fill out this field.", validationMessage, "Validation message is not matched");
        return this;
    }

    @Step("Verify Subject field validation message")
    public ContactUsPage verifySubjectFieldValidationMessage() {
        String validationMessage = (String) page.locator(subjectInput).evaluate("element => element.validationMessage");
        Assertions.assertEquals("Please fill out this field.", validationMessage, "Validation message is not matched");
        return this;
    }

    @Step("Verify Message field validation message")
    public ContactUsPage verifyMessageFieldValidationMessage() {
        String validationMessage = (String) page.locator(messageInput).evaluate("element => element.validationMessage");
        Assertions.assertEquals("Please fill out this field.", validationMessage, "Validation message is not matched");
        return this;
    }

}
