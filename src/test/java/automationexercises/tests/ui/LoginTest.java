package automationexercises.tests.ui;

import automationexercises.apis.UserManagementAPI;
import automationexercises.base.BaseTest;
import automationexercises.pages.SignupLoginPage;
import automationexercises.utils.TimeManager;
import automationexercises.utils.dataReader.JsonReader;
import io.qameta.allure.*;
import org.testng.annotations.Test;

@Epic("Automation Exercise")
@Feature("UI User Management")
@Story("User Login")
@Severity(SeverityLevel.CRITICAL)
@Owner("Omar")
public class LoginTest extends BaseTest {
        String timestamp = TimeManager.getSimpleTimestamp();
        JsonReader testData = new JsonReader("login-data");
        // Tests

        @Description("Verify user can login with valid credentials")
        @Test(groups = { "login", "regression" })
        public void loginWithValidCredentialsTest() {
                // Test logic for logging in with valid credentials
                new UserManagementAPI().createRegisterUserAccount(
                                testData.getJsonData("name"),
                                testData.getJsonData("email") + timestamp + "@gmail.com",
                                testData.getJsonData("password"),
                                testData.getJsonData("firstName"),
                                testData.getJsonData("lastName")).verifyUserCreatedSuccessfully();

                new SignupLoginPage(page).navigate()
                                .enterLoginEmail(testData.getJsonData("email") + timestamp + "@gmail.com")
                                .enterLoginPassword(testData.getJsonData("password"))
                                .clickLoginButton().navigationBar
                                .verifyLoggedInUserName(testData.getJsonData("name"));
                new UserManagementAPI().deleteUserAccount(
                                testData.getJsonData("email") + timestamp + "@gmail.com",
                                testData.getJsonData("password"))
                                .verifyUserDeletedSuccessfully();
        }

        @Description("Verify user can't login with invalid email")
        @Test(groups = { "login", "regression" })
        public void loginWithInvalidEmailTest() {
                // Test logic for logging in with an invalid email
                new UserManagementAPI().createRegisterUserAccount(
                                testData.getJsonData("name"),
                                testData.getJsonData("email") + timestamp + "@gmail.com",
                                testData.getJsonData("password"),
                                testData.getJsonData("firstName"),
                                testData.getJsonData("lastName")).verifyUserCreatedSuccessfully();

                new SignupLoginPage(page).navigate()
                                .enterLoginEmail(testData.getJsonData("email") + timestamp + timestamp + "@gmail.com")
                                .enterLoginPassword(testData.getJsonData("password"))
                                .clickLoginButton()
                                .verifyLoginErrorMessage(testData.getJsonData("messages.error"));
                new UserManagementAPI().deleteUserAccount(
                                testData.getJsonData("email") + timestamp + "@gmail.com",
                                testData.getJsonData("password"))
                                .verifyUserDeletedSuccessfully();

        }

        @Description("Verify user can't login with incorrect password")
        @Test(groups = { "login", "regression" })
        public void loginWithIncorrectPasswordTest() {
                // Test logic for logging in with an incorrect password
                new UserManagementAPI().createRegisterUserAccount(
                                testData.getJsonData("name"),
                                testData.getJsonData("email") + timestamp + "@gmail.com",
                                testData.getJsonData("password"),
                                testData.getJsonData("firstName"),
                                testData.getJsonData("lastName")).verifyUserCreatedSuccessfully();

                new SignupLoginPage(page).navigate()
                                .enterLoginEmail(testData.getJsonData("email") + timestamp + "@gmail.com")
                                .enterLoginPassword(testData.getJsonData("password") + timestamp)
                                .clickLoginButton()
                                .verifyLoginErrorMessage(testData.getJsonData("messages.error"));

                new UserManagementAPI().deleteUserAccount(
                                testData.getJsonData("email") + timestamp + "@gmail.com",
                                testData.getJsonData("password"))
                                .verifyUserDeletedSuccessfully();
        }

        @Description("Verify user can logout")
        @Test(groups = { "login", "regression" })
        public void logoutTest() {
                new UserManagementAPI().createRegisterUserAccount(
                                testData.getJsonData("name"),
                                testData.getJsonData("email") + timestamp + "@gmail.com",
                                testData.getJsonData("password"),
                                testData.getJsonData("firstName"),
                                testData.getJsonData("lastName")).verifyUserCreatedSuccessfully();

                new SignupLoginPage(page).navigate()
                                .enterLoginEmail(testData.getJsonData("email") + timestamp + "@gmail.com")
                                .enterLoginPassword(testData.getJsonData("password"))
                                .clickLoginButton().navigationBar
                                .verifyLoggedInUserName(testData.getJsonData("name"))
                                .clickOnLogoutButton().navigationBar
                                .verifyLogoutButtonNotVisible()
                                .verifySignupLabelVisible();

                new UserManagementAPI().deleteUserAccount(
                                testData.getJsonData("email") + timestamp + "@gmail.com",
                                testData.getJsonData("password"))
                                .verifyUserDeletedSuccessfully();

        }

}
