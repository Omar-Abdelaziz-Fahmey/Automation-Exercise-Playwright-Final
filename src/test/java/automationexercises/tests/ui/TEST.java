package automationexercises.tests.ui;

import automationexercises.apis.UserManagementAPI;
import automationexercises.pages.components.NavigationBarComponent;
import automationexercises.utils.TimeManager;
import automationexercises.utils.dataReader.JsonReader;
import com.microsoft.playwright.Locator;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import automationexercises.base.BaseTest;
import automationexercises.pages.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

@Epic("Automation Exercise")
@Feature("Testing Various UI Components and User Management")
public class TEST extends BaseTest {

    JsonReader testData = new JsonReader("register-data");

    String timestamp = TimeManager.getSimpleTimestamp();

    @Test
    @Story("UI Verifications")
    @Description("")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("Omar")
    public void testCaseForVerifications() {
        new NavigationBarComponent(page).navigate()
                .clickHomeButton()
                .verifyHomePageVisible();
    }



    @Test
    public void testCaseForSignUpLoginPage(){
        new UserManagementAPI().createRegisterUserAccount(
                testData.getJsonData("name"),
                testData.getJsonData("email") + timestamp + "@gmail.com",
                testData.getJsonData("password"),
                testData.getJsonData("firstName"),
                testData.getJsonData("lastName")
        ).verifyUserCreatedSuccessfully();

        new SignupLoginPage(page).navigate()
                .enterLoginEmail(testData.getJsonData("email") + timestamp + "@gmail.com")
                .enterLoginPassword(testData.getJsonData("password"))
                .clickLoginButton()
                .navigationBar
                .verifyLoggedInUserName(testData.getJsonData("name"))
                .clickOnLogoutButton()
                .navigationBar
                .verifyLogoutButtonNotVisible()
                .verifySignupLabelVisible();

        new UserManagementAPI().deleteUserAccount(
                        testData.getJsonData("email") + timestamp + "@gmail.com",
                        testData.getJsonData("password"))
                .verifyUserDeletedSuccessfully();

    }


    @Test
    public void testCaseForRegisterPage(){

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



}
