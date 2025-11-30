package automationexercises.tests.ui;

import automationexercises.PlaywrightManager;
import automationexercises.apis.UserManagementAPI;
import automationexercises.base.BaseTest;
import automationexercises.pages.CartPage;
import automationexercises.pages.ProductsPage;
import automationexercises.pages.SignupLoginPage;
import automationexercises.utils.TimeManager;
import automationexercises.utils.dataReader.JsonReader;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Epic("Checkout Management")
@Feature("UI Checkout Management")
@Owner("Omar")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CheckoutTest extends BaseTest {
    private static final String timestamp = TimeManager.getSimpleTimestamp();
    JsonReader testData = new JsonReader("checkout-data");

    @Test
    @Order(1)
    @Story("Test Data Setup")
    @Description("Create a new user account via API as a precondition for checkout flow testing")
    @Severity(SeverityLevel.CRITICAL)
    public void registerNewAccount() {
        System.out.println(timestamp);
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
    @Story("User Authentication")
    @Description("Verify that user can successfully login with valid credentials and username is displayed correctly")
    @Severity(SeverityLevel.CRITICAL)
    public void loginToAccount() {
        System.out.println(timestamp);

        new SignupLoginPage(page).navigate()
                .enterLoginEmail(testData.getJsonData("email") + timestamp + "@gmail.com")
                .enterLoginPassword(testData.getJsonData("password"))
                .clickLoginButton()
                .navigationBar
                .verifyLoggedInUserName(testData.getJsonData("name"));
    }

    @Test
    @Order(3)
    @Story("Shopping Cart Management")
    @Description("Verify that products can be added to cart and cart displays correct product details including name, price, quantity and total")
    @Severity(SeverityLevel.CRITICAL)
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
    @Story("Checkout Process")
    @Description("Verify complete checkout process including delivery and billing address validation for logged-in user with products in cart")
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
    @Story("Test Data Cleanup")
    @Description("Delete the test user account created during test execution to maintain clean test environment")
    @Severity(SeverityLevel.MINOR)
    public void deleteAccountAsPostCondition() {
        new UserManagementAPI()
                .deleteUserAccount(testData.getJsonData("email") + timestamp + "@gmail.com",
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