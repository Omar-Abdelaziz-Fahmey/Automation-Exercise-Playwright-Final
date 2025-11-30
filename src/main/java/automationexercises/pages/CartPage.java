package automationexercises.pages;


import automationexercises.utils.dataReader.PropertyReader;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitUntilState;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;

public class CartPage {
    private final Page page;

    public CartPage(Page page) {
        this.page = page;
    }


    //vars
    private String cartEndpoint = "/view_cart";

    //locators

    private final String proceedToCheckoutButton = "//a[.='Proceed To Checkout']";
    private final String registerLoginButton = "//a[.='Register / Login']";

    //dynamic locators
    private String productName(String productName) {
        return "(//h4  /a[.='" + productName + "'])[1]";
    }

    private String productPrice(String productName) {
        return "(//h4  /a[.='" + productName + "'] //following::td[@class='cart_price'] /p)[1]";
    }

    private String productQuantity(String productName) {
        return "(//h4  /a[.='" + productName + "'] //following::td[@class='cart_quantity'] /button)[1]";
    }

    private String productTotal(String productName) {
        return "(//h4  /a[.='" + productName + "'] //following::td[@class='cart_total'] /p)[1]";
    }

    private String removeProductDL(String productName) {
        return "(//h4  /a[.='" + productName + "'] //following::td[@class='cart_delete'] /a)[1]";
    }


    //Actions
    @Step("Navigate to CartPage")
    public CartPage navigate() {
        page.navigate(PropertyReader.getProperty("baseUrlWeb") + cartEndpoint,
                new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        return this;
    }

    @Step("Click on Proceed To Checkout Button")
    public CheckoutPage clickOnProceedToCheckout() {
        page.locator(proceedToCheckoutButton).click();
        return new CheckoutPage(page);
    }

    @Step("Remove Product From Cart")
    public CartPage removeProduct(String productName) {
        page.locator(removeProductDL(productName)).click();
        return this;
    }

    @Step("Click on Proceed To Checkout Button Without Register")
    public CartPage clickOnProceedToCheckoutWithOutRegister() {
        page.locator(proceedToCheckoutButton).click();
        return this;
    }

    @Step("Click on Register / Login Button")
    public SignupLoginPage clickOnRegisterLogin() {
        page.locator(registerLoginButton).click();
        return new SignupLoginPage(page);
    }


    //Validations
    @Step("Verify  Product Details On Cart")
    public CartPage verifyProductDetailsOnCart(String productName, String productPrice, String productQuantity, String productTotal) {
        String actualProductName = page.locator(productName(productName)).innerText();
        String actualProductPrice = page.locator(productPrice(productName)).innerText();
        String actualProductQuantity = page.locator(productQuantity(productName)).innerText();
        String actualProductTotal = page.locator(productTotal(productName)).innerText();
        Assertions.assertEquals(productName, actualProductName, " Product Name is not matched");
        Assertions.assertEquals(productPrice, actualProductPrice, " Product Price is not matched");
        Assertions.assertEquals(productQuantity, actualProductQuantity, " Product Quantity is not matched");
        Assertions.assertEquals(productTotal, actualProductTotal, " Product Total is not matched");
        return this;
    }
}
