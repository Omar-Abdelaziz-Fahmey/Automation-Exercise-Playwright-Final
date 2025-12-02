package automationexercises.tests.ui;

import automationexercises.PlaywrightManager;
import automationexercises.apis.UserManagementAPI;
import automationexercises.base.BaseTest;
import automationexercises.pages.*;
import automationexercises.utils.TimeManager;
import automationexercises.utils.dataReader.JsonReader;
import io.qameta.allure.*;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Epic("Automation Exercise")
@Feature("UI Full E2E Scenario")
@Story("Full E2E Flow")
@Severity(SeverityLevel.CRITICAL)
@Owner("Omar")
public class FullE2ESenarioTest_001 extends BaseTest {
    private final static String timestamp = TimeManager.getSimpleTimestamp();
    JsonReader testData = new JsonReader("full-e2e-data");



    @Test(groups = { "e2e", "regression" })
    @Story("Full E2E Flow")
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
    @Story("Full E2E Flow")
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
    @Story("Full E2E Flow")
    @Description("Add 4 different products to cart")
    @Severity(SeverityLevel.CRITICAL)
    public void addProductsToCart() {
        ProductsPage productsPage = new ProductsPage(page);
        productsPage.navigate();

        for (int i = 0; i < 4; i++) {
            String productName = testData.getJsonData("products[" + i + "].name");
            productsPage
                    .clickOnAddToCart(productName)
                    .validateProductAddedToCart(testData.getJsonData("messages.cartAdded"))
                    .clickOnContinueShopping();
        }

        productsPage.navigationBar.clickOnCartButton();

        CartPage cartPage = new CartPage(page);
        for (int i = 0; i < 4; i++) {
            cartPage.verifyProductDetailsOnCart(
                    testData.getJsonData("products[" + i + "].name"),
                    testData.getJsonData("products[" + i + "].price"),
                    testData.getJsonData("products[" + i + "].quantity"),
                    testData.getJsonData("products[" + i + "].total"));
        }
    }

    @Test(dependsOnMethods = "addProductsToCart", groups = { "e2e", "regression" })
    @Story("Full E2E Flow")
    @Description("Proceed to checkout")
    @Severity(SeverityLevel.CRITICAL)
    public void checkout() {
        new CartPage(page)
                .clickOnProceedToCheckout()
                .verifyDeliveryAddress(
                        testData.getJsonData("titleMale"),
                        testData.getJsonData("firstName"),
                        testData.getJsonData("lastName"),
                        testData.getJsonData("companyName"),
                        testData.getJsonData("address1"),
                        testData.getJsonData("address2"),
                        testData.getJsonData("city"),
                        testData.getJsonData("state"),
                        testData.getJsonData("zipcode"),
                        testData.getJsonData("country"),
                        testData.getJsonData("mobileNumber"))
                .verifyBillingAddress(
                        testData.getJsonData("titleMale"),
                        testData.getJsonData("firstName"),
                        testData.getJsonData("lastName"),
                        testData.getJsonData("companyName"),
                        testData.getJsonData("address1"),
                        testData.getJsonData("address2"),
                        testData.getJsonData("city"),
                        testData.getJsonData("state"),
                        testData.getJsonData("zipcode"),
                        testData.getJsonData("country"),
                        testData.getJsonData("mobileNumber"));
    }

    @Test(dependsOnMethods = "checkout", groups = { "e2e", "regression" })
    @Story("Full E2E Flow")
    @Description("Make payment")
    @Severity(SeverityLevel.CRITICAL)
    public void paymentTest() {
        new CheckoutPage(page)
                .clickOnPlaceOrder()
                .fillCardInfo(testData.getJsonData("card.cardName"), testData.getJsonData("card.cardNumber"),
                        testData.getJsonData("card.cvc"), testData.getJsonData("card.exMonth"),
                        testData.getJsonData("card.exYear"))
                .verifyPaymentSuccessMessage(testData.getJsonData("messages.paymentSuccess"));
    }

    @Test(dependsOnMethods = "paymentTest", groups = { "e2e", "regression" })
    @Story("Full E2E Flow")
    @Description("Download invoice")
    @Severity(SeverityLevel.NORMAL)
    public void downloadInvoice() {
        new PaymentPage(page)
                .clickOnDownloadInvoiceButton()
                .verifyDownloadedFile(testData.getJsonData("invoiceName"));
    }

    @Test(dependsOnMethods = "downloadInvoice", groups = { "e2e", "regression" })
    @Story("Full E2E Flow")
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
