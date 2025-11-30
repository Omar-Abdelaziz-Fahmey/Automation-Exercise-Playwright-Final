package automationexercises.tests.ui;

import automationexercises.PlaywrightManager;
import automationexercises.apis.UserManagementAPI;
import automationexercises.base.BaseTest;
import automationexercises.pages.CartPage;
import automationexercises.pages.CheckoutPage;
import automationexercises.pages.ProductsPage;
import automationexercises.pages.SignupLoginPage;
import automationexercises.utils.TimeManager;
import automationexercises.utils.dataReader.JsonReader;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Epic("Automation Exercise")
@Feature("UI Payment")
@Story("Payment")
@Severity(SeverityLevel.CRITICAL)
@Owner("Omar")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PaymentTest extends BaseTest {
    private final static String timestamp = TimeManager.getSimpleTimestamp();
    JsonReader testData = new JsonReader("checkout-data");

    @Test
    @Order(1)
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
                        testData.getJsonData("mobileNumber")
                )
                .verifyUserCreatedSuccessfully();
    }

    @Test
    @Order(2)
    public void loginToAccount() {
        new SignupLoginPage(page).navigate()
                .enterLoginEmail(testData.getJsonData("email") + timestamp + "@gmail.com")
                .enterLoginPassword(testData.getJsonData("password"))
                .clickLoginButton()
                .navigationBar
                .verifyLoggedInUserName(testData.getJsonData("name"));
    }

    @Test
    @Order(3)
    public void addProductToCart() {
        new ProductsPage(page)
                .navigate()
                .clickOnAddToCart(testData.getJsonData("product.name"))
                .validateProductAddedToCart(testData.getJsonData("messages.cartAdded"))
                .clickOnViewCart()
                .verifyProductDetailsOnCart(
                        testData.getJsonData("product.name"),
                        testData.getJsonData("product.price"),
                        testData.getJsonData("product.quantity"),
                        testData.getJsonData("product.total")
                );
    }

    @Test
    @Order(4)
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
                        testData.getJsonData("mobileNumber")
                )
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
                        testData.getJsonData("mobileNumber")
                );
    }

    @Test
    @Order(5)
    public void paymentTest() {
        new CheckoutPage(page)
                .clickOnPlaceOrder()
                .fillCardInfo(testData.getJsonData("card.cardName")
                        ,testData.getJsonData("card.cardNumber"),
                        testData.getJsonData("card.cvc")
                                , testData.getJsonData("card.exMonth")
                                , testData.getJsonData("card.exYear")
                )

                .verifyPaymentSuccessMessage(testData.getJsonData("messages.paymentSuccess"));
    }

    @Test
    @Order(6)
    public void deleteAccountAsPostCondition() {
        new UserManagementAPI()
                .deleteUserAccount( testData.getJsonData("email") + timestamp + "@gmail.com",
                        testData.getJsonData("password"
                        ))
                .verifyUserDeletedSuccessfully();
    }


    @AfterEach
    @Step("Close page and capture final state")
    public void tearDown(TestInfo testInfo) {
        if (page != null) {
            // Capture screenshot before closing (useful for both passed and failed tests)
            try {
                byte[] screenshot = page.screenshot();
                String screenshotName = testInfo.getDisplayName() + " - Final State";
                Allure.addAttachment(screenshotName, "image/png",
                        new ByteArrayInputStream(screenshot), ".png");
            } catch (Exception e) {
                System.err.println("Failed to capture screenshot: " + e.getMessage());
            }

            // Add completion timestamp
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            // Allure.parameter("Test End Time", timestamp);

            //PlaywrightManager.closePage();
        }
    }

    @AfterAll
    @Step("Stop Playwright and cleanup resources")
    public static void tearDownAll() {
        PlaywrightManager.closePage();
        PlaywrightManager.stop();
    }


}