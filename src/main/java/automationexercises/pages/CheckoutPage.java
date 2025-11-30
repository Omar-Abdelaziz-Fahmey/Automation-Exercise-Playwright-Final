package automationexercises.pages;

import automationexercises.utils.dataReader.PropertyReader;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitUntilState;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;

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

                Assertions.assertEquals((title + ". " + fName + " " + lName), page.locator(deliveryName).innerText(),
                                " Delivery Name is not matched");
                Assertions.assertEquals(company, page.locator(deliveryCompany).innerText(),
                                "Delivery Company is not matched");
                Assertions.assertEquals(address1, page.locator(deliveryAddress1).innerText(),
                                " Delivery Address1 is not matched");
                Assertions.assertEquals(address2, page.locator(deliveryAddress2).innerText(),
                                " Delivery Address2 is not matched");
                Assertions.assertEquals((city + " " + state + " " + zip),
                                page.locator(deliveryCityStateZip).innerText(),
                                " Delivery CityStateZip is not matched");
                Assertions.assertEquals(country, page.locator(deliveryCountry).innerText(),
                                " Delivery Country is not matched");
                Assertions.assertEquals(phone, page.locator(deliveryPhone).innerText(),
                                " Delivery Phone is not matched");
                return this;
        }

        @Step("Verify Billing Address")
        public CheckoutPage verifyBillingAddress(String title, String fName, String lName, String company,
                        String address1, String address2,
                        String city, String state, String zip, String country, String phone) {

                Assertions.assertEquals((title + ". " + fName + " " + lName), page.locator(deliveryName).innerText(),
                                " Billing Name is not matched");
                Assertions.assertEquals(company, page.locator(billingCompany).innerText(),
                                "Billing Company is not matched");
                Assertions.assertEquals(address1, page.locator(deliveryAddress1).innerText(),
                                " Billing Address1 is not matched");
                Assertions.assertEquals(address2, page.locator(billingAddress2).innerText(),
                                " Billing Address2 is not matched");
                Assertions.assertEquals((city + " " + state + " " + zip), page.locator(billingCityStateZip).innerText(),
                                " Billing CityStateZip is not matched");
                Assertions.assertEquals(country, page.locator(billingCountry).innerText(),
                                " Billing Country is not matched");
                Assertions.assertEquals(phone, page.locator(billingPhone).innerText(), " Billing Phone is not matched");

                return this;
        }

}
