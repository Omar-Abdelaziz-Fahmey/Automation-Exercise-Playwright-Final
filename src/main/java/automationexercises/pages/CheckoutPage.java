package automationexercises.pages;

import automationexercises.utils.dataReader.PropertyReader;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitUntilState;
import io.qameta.allure.Step;
import org.testng.Assert;

public class CheckoutPage {

        private final Page page;

        public CheckoutPage(Page page) {
                this.page = page;
        }

        // vars
        private String checkoutEndpoint = "/checkout";

        // locators
        private final String placeOrderButton = "//a[.='Place Order']";
        // delivery address
        private final String deliveryName = "//ul[@id='address_delivery'] /li[@class='address_firstname address_lastname']";
        private final String deliveryCompany = "//ul[@id='address_delivery'] /li[@class='address_address1 address_address2'][1]";
        private final String deliveryAddress1 = "//ul[@id='address_delivery'] /li[@class='address_address1 address_address2'][2]";
        private final String deliveryAddress2 = "//ul[@id='address_delivery'] /li[@class='address_address1 address_address2'][3]";
        private final String deliveryCityStateZip = "//ul[@id='address_delivery'] /li[@class='address_city address_state_name address_postcode']";
        private final String deliveryCountry = "//ul[@id='address_delivery'] /li[@class='address_country_name']";
        private final String deliveryPhone = "//ul[@id='address_delivery'] /li[@class='address_phone']";
        // billing address
        private final String billingName = "//ul[@id='address_invoice'] /li[@class='address_firstname address_lastname']";
        private final String billingCompany = "//ul[@id='address_invoice'] /li[@class='address_address1 address_address2'][1]";
        private final String billingAddress1 = "//ul[@id='address_invoice'] /li[@class='address_address1 address_address2'][2]";
        private final String billingAddress2 = "//ul[@id='address_invoice'] /li[@class='address_address1 address_address2'][3]";
        private final String billingCityStateZip = "//ul[@id='address_invoice'] /li[@class='address_city address_state_name address_postcode']";
        private final String billingCountry = "//ul[@id='address_invoice'] /li[@class='address_country_name']";
        private final String billingPhone = "//ul[@id='address_invoice'] /li[@class='address_phone']";

        // actions
        @Step("Navigate To Checkout Page")
        public CheckoutPage navigate() {
                page.navigate(PropertyReader.getProperty("baseUrlWeb") + checkoutEndpoint,
                                new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
                return this;
        }

        @Step("Click On Place Order Button")
        public PaymentPage clickOnPlaceOrder() {
                page.locator(placeOrderButton).click();
                return new PaymentPage(page);
        }

        // validations
        @Step("Verify Delivery Address")
        public CheckoutPage verifyDeliveryAddress(String title, String fName, String lName, String company,
                        String address1, String address2,
                        String city, String state, String zip, String country, String phone) {

                Assert.assertEquals(page.locator(deliveryName).innerText(), (title + ". " + fName + " " + lName),
                                " Delivery Name is not matched");
                Assert.assertEquals(page.locator(deliveryCompany).innerText(), company,
                                "Delivery Company is not matched");
                Assert.assertEquals(page.locator(deliveryAddress1).innerText(), address1,
                                " Delivery Address1 is not matched");
                Assert.assertEquals(page.locator(deliveryAddress2).innerText(), address2,
                                " Delivery Address2 is not matched");
                Assert.assertEquals(page.locator(deliveryCityStateZip).innerText(), (city + " " + state + " " + zip),
                                " Delivery CityStateZip is not matched");
                Assert.assertEquals(page.locator(deliveryCountry).innerText(), country,
                                " Delivery Country is not matched");
                Assert.assertEquals(page.locator(deliveryPhone).innerText(), phone,
                                " Delivery Phone is not matched");
                return this;
        }

        @Step("Verify Billing Address")
        public CheckoutPage verifyBillingAddress(String title, String fName, String lName, String company,
                        String address1, String address2,
                        String city, String state, String zip, String country, String phone) {

                Assert.assertEquals(page.locator(deliveryName).innerText(), (title + ". " + fName + " " + lName),
                                " Billing Name is not matched");
                Assert.assertEquals(page.locator(billingCompany).innerText(), company,
                                "Billing Company is not matched");
                Assert.assertEquals(page.locator(deliveryAddress1).innerText(), address1,
                                " Billing Address1 is not matched");
                Assert.assertEquals(page.locator(billingAddress2).innerText(), address2,
                                " Billing Address2 is not matched");
                Assert.assertEquals(page.locator(deliveryCityStateZip).innerText(), (city + " " + state + " " + zip),
                                " Billing CityStateZip is not matched");
                Assert.assertEquals(page.locator(billingCountry).innerText(), country,
                                " Billing Country is not matched");
                Assert.assertEquals(page.locator(billingPhone).innerText(), phone, " Billing Phone is not matched");

                return this;
        }

}
