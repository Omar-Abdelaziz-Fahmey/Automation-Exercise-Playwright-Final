package automationexercises.tests.ui;

import automationexercises.PlaywrightManager;
import automationexercises.apis.UserManagementAPI;
import automationexercises.base.BaseTest;
import automationexercises.pages.*;
import automationexercises.utils.TimeManager;
import automationexercises.utils.dataReader.JsonReader;
import io.qameta.allure.*;
import org.testng.annotations.*;
import org.testng.ITestResult;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Epic("Automation Exercise")
@Feature("UI Invoice Management")
@Owner("Omar")
public class InvoiceTest extends BaseTest {
        private final static String timestamp = TimeManager.getSimpleTimestamp();
        JsonReader testData = new JsonReader("checkout-data");

        @Test(groups = { "invoice", "regression" })
        @Story("Invoice Flow 1: Register -> Checkout")
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

        @Test(dependsOnMethods = "registerNewAccount", groups = { "invoice", "regression" })
        @Story("Invoice Flow 1: Register -> Checkout")
        @Description("Login to the account")
        @Severity(SeverityLevel.CRITICAL)
        public void loginToAccount() {
                new SignupLoginPage(page).navigate()
                                .enterLoginEmail(testData.getJsonData("email") + timestamp + "@gmail.com")
                                .enterLoginPassword(testData.getJsonData("password"))
                                .clickLoginButton().navigationBar
                                .verifyLoggedInUserName(testData.getJsonData("name"));
        }

        @Test(dependsOnMethods = "loginToAccount", groups = { "invoice", "regression" })
        @Story("Invoice Flow 1: Register -> Checkout")
        @Description("Add product to cart")
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
                                                testData.getJsonData("product.total"));
        }

        @Test(dependsOnMethods = "addProductToCart", groups = { "invoice", "regression" })
        @Story("Invoice Flow 1: Register -> Checkout")
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

        @Test(dependsOnMethods = "checkout", groups = { "invoice", "regression" })
        @Story("Invoice Flow 1: Register -> Checkout")
        @Description("Make payment")
        @Severity(SeverityLevel.CRITICAL)
        public void paymentTest() {
                new CheckoutPage(page)
                                .clickOnPlaceOrder()
                                .fillCardInfo(testData.getJsonData("card.cardName"),
                                                testData.getJsonData("card.cardNumber"),
                                                testData.getJsonData("card.cvc"), testData.getJsonData("card.exMonth"),
                                                testData.getJsonData("card.exYear"))

                                .verifyPaymentSuccessMessage(testData.getJsonData("messages.paymentSuccess"));
        }

        @Test(dependsOnMethods = "paymentTest", groups = { "invoice", "regression" })
        @Story("Invoice Flow 1: Register -> Checkout")
        @Description("Download invoice")
        @Severity(SeverityLevel.NORMAL)
        public void downloadInvoice() {
                new PaymentPage(page)
                                .clickOnDownloadInvoiceButton()
                                .verifyDownloadedFile(testData.getJsonData("invoiceName"));
        }

        @Test(dependsOnMethods = "downloadInvoice", groups = { "invoice", "regression" })
        @Story("Invoice Flow 1: Register -> Checkout")
        @Description("Delete account")
        @Severity(SeverityLevel.MINOR)
        public void deleteAccountAsPostCondition() {
                new UserManagementAPI()
                                .deleteUserAccount(testData.getJsonData("email") + timestamp + "@gmail.com",
                                                testData.getJsonData("password"))
                                .verifyUserDeletedSuccessfully();
        }

        // another flow starting from adding product to cart without logging in

        @Test(groups = { "invoice", "regression" })
        @Story("Invoice Flow 2: Checkout -> Register")
        @Description("Add product to cart without login")
        @Severity(SeverityLevel.CRITICAL)
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

        @Test(dependsOnMethods = "verifyProductDetailsOnCartWithOutLogInTC", groups = { "invoice", "regression" })
        @Story("Invoice Flow 2: Checkout -> Register")
        @Description("Proceed to checkout without register")
        @Severity(SeverityLevel.CRITICAL)
        public void checkoutWithoutRegister() {
                new CartPage(page)
                                .clickOnProceedToCheckoutWithOutRegister()
                                .clickOnRegisterLogin()
                                .verifySignupLabelVisible();

        }

        @Test(dependsOnMethods = "checkoutWithoutRegister", groups = { "invoice", "regression" })
        @Story("Invoice Flow 2: Checkout -> Register")
        @Description("Register during checkout")
        @Severity(SeverityLevel.CRITICAL)
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

        @Test(dependsOnMethods = "registerDuringCheckout", groups = { "invoice", "regression" })
        @Story("Invoice Flow 2: Checkout -> Register")
        @Description("Complete checkout after registering")
        @Severity(SeverityLevel.CRITICAL)
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

        @Test(dependsOnMethods = "completeCheckoutAfterRegistering", groups = { "invoice", "regression" })
        @Story("Invoice Flow 2: Checkout -> Register")
        @Description("Make payment after registering")
        @Severity(SeverityLevel.CRITICAL)
        public void paymentAfterRegisteringTest() {
                new CheckoutPage(page)
                                .clickOnPlaceOrder()
                                .fillCardInfo(testData.getJsonData("card.cardName"),
                                                testData.getJsonData("card.cardNumber"),
                                                testData.getJsonData("card.cvc"), testData.getJsonData("card.exMonth"),
                                                testData.getJsonData("card.exYear"))
                                .verifyPaymentSuccessMessage(testData.getJsonData("messages.paymentSuccess"));
        }

        @Test(dependsOnMethods = "paymentAfterRegisteringTest", groups = { "invoice", "regression" })
        @Story("Invoice Flow 2: Checkout -> Register")
        @Description("Download invoice after registering")
        @Severity(SeverityLevel.NORMAL)
        public void downloadInvoiceAfterRegisteringTest() {
                new PaymentPage(page)
                                .clickOnDownloadInvoiceButton()
                                .verifyDownloadedFile(testData.getJsonData("invoiceName"));
        }

        @Test(dependsOnMethods = "downloadInvoiceAfterRegisteringTest", groups = { "invoice", "regression" })
        @Story("Invoice Flow 2: Checkout -> Register")
        @Description("Delete account after registering")
        @Severity(SeverityLevel.MINOR)
        public void deleteAccountAsPostConditionAfterRegisteringTest() {
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