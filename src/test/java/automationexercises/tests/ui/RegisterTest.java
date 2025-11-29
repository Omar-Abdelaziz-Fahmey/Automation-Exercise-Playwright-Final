package automationexercises.tests.ui;

import automationexercises.apis.UserManagementAPI;
import automationexercises.base.BaseTest;
import automationexercises.pages.SignupLoginPage;
import automationexercises.pages.SignupPage;
import automationexercises.utils.TimeManager;
import automationexercises.utils.dataReader.JsonReader;
import io.qameta.allure.*;
import org.junit.jupiter.api.Test;


@Epic("Automation Exercise")
@Feature("UI User Management")
@Owner("Omar")
public class RegisterTest extends BaseTest {
    String timestamp = TimeManager.getSimpleTimestamp();
    JsonReader testData = new JsonReader("register-data");

    //Test
    @Test
    @Story("User Registration - Happy Path")
    @Description("Verify that a new user can successfully register with valid data and complete the registration process")
    @Severity(SeverityLevel.CRITICAL)
    public void verifyUserCanRegisterSuccessfullyTC() {
        new SignupLoginPage(page).navigate()
                .enterSignupName(testData.getJsonData("name"))
                .enterSignupEmail(testData.getJsonData("email") + timestamp + "@gmail.com")
                .clickSignupButton();

        new SignupPage(page)
                .chooseTitle(testData.getJsonData("titleMale"))
                .enterPassword(testData.getJsonData("password"))
                .selectDateOfBirth(
                        testData.getJsonData("day"),
                        testData.getJsonData("month"),
                        testData.getJsonData("year")
                )
                .subscribeToNewsletter()
                .receiveSpecialOffers()
                .enterFirstName(testData.getJsonData("firstName"))
                .enterLastName(testData.getJsonData("lastName"))
                .enterCompany(testData.getJsonData("companyName"))
                .enterAddress1(testData.getJsonData("address1"))
                .enterAddress2(testData.getJsonData("address2"))
                .selectCountry(testData.getJsonData("country"))
                .enterState(testData.getJsonData("state"))
                .enterCity(testData.getJsonData("city"))
                .enterZipcode(testData.getJsonData("zipcode"))
                .enterMobileNumber(testData.getJsonData("mobileNumber"))
                .clickCreateAccountButton()
                .verifyAccountCreated();

        new UserManagementAPI().deleteUserAccount(
                        testData.getJsonData("email") + timestamp + "@gmail.com",
                        testData.getJsonData("password"))
                .verifyUserDeletedSuccessfully();
    }

    @Test
    @Story("User Registration - Negative Scenarios")
    @Description("Verify that appropriate error message is displayed when attempting to register with an already existing email address")
    @Severity(SeverityLevel.CRITICAL)
    public void verifyErrorMessageWhenRegisteringWithExistingEmailTC() {
        new UserManagementAPI().createRegisterUserAccount(
                        testData.getJsonData("name"),
                        testData.getJsonData("email") + timestamp + "@gmail.com",
                        testData.getJsonData("password"),
                        testData.getJsonData("titleMale"),
                        testData.getJsonData("day"),
                        testData.getJsonData("month"),
                        testData.getJsonData("year"),
                        testData.getJsonData("firstName"),
                        testData.getJsonData("lastName"),
                        testData.getJsonData("companyName"),
                        testData.getJsonData("address1"),
                        testData.getJsonData("address2"),
                        testData.getJsonData("country"),
                        testData.getJsonData("state"),
                        testData.getJsonData("city"),
                        testData.getJsonData("zipcode"),
                        testData.getJsonData("mobileNumber")
                )
                .verifyUserCreatedSuccessfully();
        new SignupLoginPage(page).navigate()
                .enterSignupEmail(testData.getJsonData("email") + timestamp + "@gmail.com")
                .enterSignupName(testData.getJsonData("name"))
                .clickSignupButton()
                .verifyRegisterErrorMessage(testData.getJsonData("messages.error"));


        new UserManagementAPI().deleteUserAccount(
                        testData.getJsonData("email") + timestamp + "@gmail.com",
                        testData.getJsonData("password"))
                .verifyUserDeletedSuccessfully();


    }


    @Test
    @Story("User Registration - Negative Scenarios")
    @Description("Verify that appropriate error message is displayed when attempting to register with an invalid email address")
    @Severity(SeverityLevel.CRITICAL)
    public void verifyErrorMessageWhenRegisteringWithInvalidEmailTC() {
        new SignupLoginPage(page).navigate()
                .enterSignupName(testData.getJsonData("name"))
                .enterSignupEmail("invalid-email")
                .clickSignupButton();
        // Since browser validation might prevent submission, we verify we are still on
        // the login page or see an error
        new SignupLoginPage(page).verifySignupLabelVisible();
    }

    @Test
    @Story("User Registration - Negative Scenarios")
    @Description("Verify that account is not created when Password field is missing")
    @Severity(SeverityLevel.CRITICAL)
    public void verifyErrorMessageWhenRegisteringWithMissingFieldsTC() {
        new SignupLoginPage(page).navigate()
                .enterSignupName(testData.getJsonData("name"))
                .enterSignupEmail(testData.getJsonData("email") + timestamp + "@gmail.com")
                .clickSignupButton();

        new SignupPage(page)
                .chooseTitle(testData.getJsonData("titleMale"))
                // Skip password to trigger missing field error
                .selectDateOfBirth(
                        testData.getJsonData("day"),
                        testData.getJsonData("month"),
                        testData.getJsonData("year"))
                .enterFirstName(testData.getJsonData("firstName"))
                .enterLastName(testData.getJsonData("lastName"))
                .enterCompany(testData.getJsonData("companyName"))
                .enterAddress1(testData.getJsonData("address1"))
                .enterAddress2(testData.getJsonData("address2"))
                .selectCountry(testData.getJsonData("country"))
                .enterState(testData.getJsonData("state"))
                .enterCity(testData.getJsonData("city"))
                .enterZipcode(testData.getJsonData("zipcode"))
                .enterMobileNumber(testData.getJsonData("mobileNumber"))
                .clickCreateAccountButton()
                .verifyOnSignupPage(); // Verify we are still on the signup page
    }

    @Test
    @Story("User Registration - Negative Scenarios")
    @Description("Verify that account is not created when Name fields are missing")
    @Severity(SeverityLevel.CRITICAL)
    public void verifyErrorMessageWhenRegisteringWithMissingNameTC() {
        new SignupLoginPage(page).navigate()
                .enterSignupName(testData.getJsonData("name"))
                .enterSignupEmail(testData.getJsonData("email") + timestamp + "@gmail.com")
                .clickSignupButton();

        new SignupPage(page)
                .chooseTitle(testData.getJsonData("titleMale"))
                .enterPassword(testData.getJsonData("password"))
                .selectDateOfBirth(
                        testData.getJsonData("day"),
                        testData.getJsonData("month"),
                        testData.getJsonData("year"))
                // Skip first and last name to trigger missing field error
                .enterCompany(testData.getJsonData("companyName"))
                .enterAddress1(testData.getJsonData("address1"))
                .enterAddress2(testData.getJsonData("address2"))
                .selectCountry(testData.getJsonData("country"))
                .enterState(testData.getJsonData("state"))
                .enterCity(testData.getJsonData("city"))
                .enterZipcode(testData.getJsonData("zipcode"))
                .enterMobileNumber(testData.getJsonData("mobileNumber"))
                .clickCreateAccountButton()
                .verifyOnSignupPage();


    }
    @Test
    @Story("User Registration - Negative Scenarios")
    @Description("Verify that account is not created with Invalid Zipcode")
    @Severity(SeverityLevel.CRITICAL)
    public void verifyErrorMessageWhenRegisteringWithInvalidZipcodeTC() {

        new SignupLoginPage(page).navigate()
                .enterSignupName(testData.getJsonData("name"))
                .enterSignupEmail(testData.getJsonData("email") + timestamp + "@gmail.com")
                .clickSignupButton();

        new SignupPage(page)
                .chooseTitle(testData.getJsonData("titleMale"))
                .enterPassword(testData.getJsonData("password"))
                .selectDateOfBirth(
                        testData.getJsonData("day"),
                        testData.getJsonData("month"),
                        testData.getJsonData("year"))
                .enterFirstName(testData.getJsonData("firstName"))
                .enterLastName(testData.getJsonData("lastName"))
                .enterCompany(testData.getJsonData("companyName"))
                .enterAddress1(testData.getJsonData("address1"))
                .enterAddress2(testData.getJsonData("address2"))
                .selectCountry(testData.getJsonData("country"))
                .enterState(testData.getJsonData("state"))
                .enterCity(testData.getJsonData("city"))
                .enterZipcode(testData.getJsonData("invalidZipcode"))
                .enterMobileNumber(testData.getJsonData("mobileNumber"))
                .clickCreateAccountButton()
                .verifyOnSignupPage();

        // Delete the account if it is registered



        new UserManagementAPI().deleteUserAccount(
                        testData.getJsonData("email") + timestamp + "@gmail.com",
                        testData.getJsonData("password"))
                .verifyUserDeletedSuccessfully();



    }



}