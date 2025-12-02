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


}