package automationexercises.tests.ui;

import automationexercises.PlaywrightManager;
import automationexercises.apis.UserManagementAPI;
import automationexercises.base.BaseTest;
import automationexercises.pages.*;
import automationexercises.utils.TimeManager;
import automationexercises.utils.dataReader.JsonReader;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Epic("Automation Exercise")
@Feature("UI Invoice")
@Story("Invoice")
@Severity(SeverityLevel.CRITICAL)
@Owner("Omar")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class InvoiceTest extends BaseTest {
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
                                testData.getJsonData("mobileNumber"))
                                .verifyUserCreatedSuccessfully();
        }

        @Test
        @Order(2)
        public void loginToAccount() {
                new SignupLoginPage(page).navigate()
                                .enterLoginEmail(testData.getJsonData("email") + timestamp + "@gmail.com")
                                .enterLoginPassword(testData.getJsonData("password"))
                                .clickLoginButton().navigationBar
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
                                                testData.getJsonData("product.total"));
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

        @Test
        @Order(5)
        public void paymentTest() {
                new CheckoutPage(page)
                                .clickOnPlaceOrder()
                                .fillCardInfo(testData.getJsonData("card.cardName"),
                                                testData.getJsonData("card.cardNumber"),
                                                testData.getJsonData("card.cvc"), testData.getJsonData("card.exMonth"),
                                                testData.getJsonData("card.exYear"))

                                .verifyPaymentSuccessMessage(testData.getJsonData("messages.paymentSuccess"));
        }

        @Test
        @Order(6)
        public void downloadInvoice() {
                new PaymentPage(page)
                                .clickOnDownloadInvoiceButton()
                                .verifyDownloadedFile(testData.getJsonData("invoiceName"));
        }

        @Test
        @Order(7)
        public void deleteAccountAsPostCondition() {
                new UserManagementAPI()
                                .deleteUserAccount(testData.getJsonData("email") + timestamp + "@gmail.com",
                                                testData.getJsonData("password"))
                                .verifyUserDeletedSuccessfully();
        }

        // another flow starting from adding product to cart without logging in

        @Test
        @Order(8)
        public void verifyProductDetailsOnCartWithOutLogInTC() {
                new ProductsPage(page).navigationBar.clickProductsButton()
                                .clickOnAddToCart(testData.getJsonData("product.name"))
                                .validateProductAddedToCart(testData.getJsonData("messages.cartAdded"))
                                .clickOnViewCart()
                                .verifyProductDetailsOnCart(
                                                testData.getJsonData("product.name"),
                                                testData.getJsonData("product.price"),
                                                testData.getJsonData("product.quantity"),
                                                testData.getJsonData("product.total"));
        }

        @Test
        @Order(9)
        public void checkoutWithoutRegister() {
                new CartPage(page)
                                .clickOnProceedToCheckoutWithOutRegister()
                                .clickOnRegisterLogin()
                                .verifySignupLabelVisible();

        }

        @Test
        @Order(10)
        public void registerDuringCheckout() {
                new SignupLoginPage(page)
                                .enterSignupEmail(testData.getJsonData("email") + timestamp + "@gmail.com")
                                .enterSignupName(testData.getJsonData("name"))
                                .clickSignupButton();

                new SignupPage(page)
                                .chooseTitle(testData.getJsonData("titleMale"))
                                .enterPassword(testData.getJsonData("password"))
                                .selectDateOfBirth(
                                                testData.getJsonData("day"),
                                                testData.getJsonData("month"),
                                                testData.getJsonData("year"))
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
        }

        @Test
        @Order(11)
        public void completeCheckoutAfterRegistering() {
                new CartPage(page)
                                .navigate()
                                .verifyProductDetailsOnCart(
                                                testData.getJsonData("product.name"),
                                                testData.getJsonData("product.price"),
                                                testData.getJsonData("product.quantity"),
                                                testData.getJsonData("product.total"))
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

        @Test
        @Order(12)
        public void paymentAfterRegisteringTest() {
                new CheckoutPage(page)
                                .clickOnPlaceOrder()
                                .fillCardInfo(testData.getJsonData("card.cardName"),
                                                testData.getJsonData("card.cardNumber"),
                                                testData.getJsonData("card.cvc"), testData.getJsonData("card.exMonth"),
                                                testData.getJsonData("card.exYear"))
                                .verifyPaymentSuccessMessage(testData.getJsonData("messages.paymentSuccess"));
        }

        @Test
        @Order(13)
        public void downloadInvoiceAfterRegisteringTest() {
                new PaymentPage(page)
                                .clickOnDownloadInvoiceButton()
                                .verifyDownloadedFile(testData.getJsonData("invoiceName"));
        }

        @Test
        @Order(14)
        public void deleteAccountAsPostConditionAfterRegisteringTest() {
                new UserManagementAPI()
                                .deleteUserAccount(testData.getJsonData("email") + timestamp + "@gmail.com",
                                                testData.getJsonData("password"))
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