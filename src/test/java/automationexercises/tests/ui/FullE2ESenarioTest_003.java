package automationexercises.tests.ui;

import automationexercises.PlaywrightManager;
import automationexercises.apis.UserManagementAPI;
import automationexercises.base.BaseTest;
import automationexercises.pages.ContactUsPage;
import automationexercises.pages.ProductsPage;
import automationexercises.pages.SignupLoginPage;
import automationexercises.utils.TimeManager;
import automationexercises.utils.dataReader.JsonReader;
import io.qameta.allure.*;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Epic("Automation Exercise")
@Feature("UI Full E2E Scenario")
@Story("Full E2E Flow 003")
@Severity(SeverityLevel.CRITICAL)
@Owner("Omar")
public class FullE2ESenarioTest_003 extends BaseTest {
    private final static String timestamp = TimeManager.getSimpleTimestamp();
    JsonReader testData = new JsonReader("full-e2e-data");



    @Test(groups = { "e2e", "regression" })
    @Story("Full E2E Flow 003")
    @Description("Register a new account")
    @Severity(SeverityLevel.CRITICAL)
    public void registerNewAccount() {
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
                testData.getJsonData("zipcode"),
                testData.getJsonData("state"),
                testData.getJsonData("city"),
                testData.getJsonData("mobileNumber"))
                .verifyUserCreatedSuccessfully();
    }

    @Test(dependsOnMethods = "registerNewAccount", groups = { "e2e", "regression" })
    @Story("Full E2E Flow 003")
    @Description("Login to the account")
    @Severity(SeverityLevel.CRITICAL)
    public void loginToAccount() {
        new SignupLoginPage(page).navigate()
                .enterLoginEmail(testData.getJsonData("email") + timestamp + "@gmail.com")
                .enterLoginPassword(testData.getJsonData("password"))
                .clickLoginButton().navigationBar
                .verifyLoggedInUserName(testData.getJsonData("name"));
    }

    @Test(dependsOnMethods = "loginToAccount", groups = { "e2e", "regression" })
    @Story("Full E2E Flow 003")
    @Description("Navigate to product page and add subscription")
    @Severity(SeverityLevel.CRITICAL)
    public void verifySubscriptionInProductPage() {
        new ProductsPage(page).navigate().subscriptionBar
                .enterSubscriptionEmail(testData.getJsonData("subscription.email"))
                .validateSubscriptionSuccessMessage(testData.getJsonData("messages.subscriptionSuccess"));
    }

    @Test(dependsOnMethods = "verifySubscriptionInProductPage", groups = { "e2e", "regression" })
    @Story("Full E2E Flow 003")
    @Description("Open product details and add review")
    @Severity(SeverityLevel.CRITICAL)
    public void addReviewToProduct() {
        new ProductsPage(page).navigate()
                .clickOnViewProduct(testData.getJsonData("products[0].name"))
                .addReview(
                        testData.getJsonData("review.name"),
                        testData.getJsonData("review.email"),
                        testData.getJsonData("review.review"))
                .verifyReviewMsg(testData.getJsonData("messages.review"));
    }

    @Test(dependsOnMethods = "addReviewToProduct", groups = { "e2e", "regression" })
    @Story("Full E2E Flow 003")
    @Description("Go to contact us page to send message")
    @Severity(SeverityLevel.CRITICAL)
    public void contactUs() {
        new ContactUsPage(page).navigate()
                .addReview(
                        testData.getJsonData("review.name"),
                        testData.getJsonData("review.email"),
                        testData.getJsonData("contactUs.subject"),
                        testData.getJsonData("contactUs.message"))
                .verifyAlertMessageAfterSubmit(testData.getJsonData("messages.alertMessage"))
                .clickSubmitButton()
                .verifySuccessMessageAfterSubmit(testData.getJsonData("messages.successMessage"));
    }

    @Test(dependsOnMethods = "contactUs", groups = { "e2e", "regression" })
    @Story("Full E2E Flow 003")
    @Description("Delete account")
    @Severity(SeverityLevel.MINOR)
    public void deleteAccount() {
        new UserManagementAPI()
                .deleteUserAccount(testData.getJsonData("email") + timestamp + "@gmail.com",
                        testData.getJsonData("password"))
                .verifyUserDeletedSuccessfully();
    }

    @AfterMethod
    @Step("Close page and capture final state")
    public void tearDown(ITestResult result) {
        if (page != null) {
            // Capture screenshot before closing (useful for both passed and failed tests)
            try {
                byte[] screenshot = page.screenshot();
                String screenshotName = result.getName() + " - Final State";
                Allure.addAttachment(screenshotName, "image/png",
                        new ByteArrayInputStream(screenshot), ".png");
            } catch (Exception e) {
                System.err.println("Failed to capture screenshot: " + e.getMessage());
            }

            // Add completion timestamp
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            // Allure.parameter("Test End Time", timestamp);

            // PlaywrightManager.closePage();
        }
    }

    @AfterClass
    @Step("Stop Playwright and cleanup resources")
    public void tearDownAll() {
        PlaywrightManager.closePage();
        PlaywrightManager.stop();
    }
}
