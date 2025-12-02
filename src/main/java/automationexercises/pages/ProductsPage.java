package automationexercises.pages;

import automationexercises.pages.components.NavigationBarComponent;
import automationexercises.pages.components.SubscriptionComponent;
import automationexercises.utils.dataReader.PropertyReader;
import automationexercises.utils.logs.LogsManager;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.TimeoutError;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.microsoft.playwright.options.WaitUntilState;
import io.qameta.allure.Step;
import org.testng.Assert;

public class ProductsPage {

    private final Page page;
    public NavigationBarComponent navigationBar;
    public SubscriptionComponent subscriptionBar;

    public ProductsPage(Page page) {
        this.page = page;
        this.navigationBar = new NavigationBarComponent(page);
        this.subscriptionBar=new SubscriptionComponent(page);

    }

    // variables
    final private String productPage = "/products";

    // locators
    private final String searchField = "Search Product";
    private final String searchButton = "#submit_search";
    private final String itemAddedLabel = ".modal-body > p";
    private final String viewCartButton = "p > [href=\"/view_cart\"]";
    private final String continueShoppingButton = ".modal-footer >button";

    // dynamic locators

    private String productName(String productName) {
        return "//div[@class='features_items'] //div[@class='overlay-content'] /p[.='" + productName + "']";
    }

    private String productPrice(String productName) {
        return "//div[@class='features_items'] //div[@class='overlay-content'] /p[.='" + productName
                + "'] //preceding-sibling::h2";
    }

    private String hoverOnProduct(String productName) {
        return "//div[@class='features_items'] //div[@class='productinfo text-center'] /p[.='" + productName + "']";
    }

    private String addToCartButton(String productName) {
        return "//div[@class='features_items'] //div[@class='overlay-content'] /p[.='" + productName
                + "'] //following-sibling::a";

    }

    private String viewProduct(String productName) {
        return "//p[.='" + productName + "'] //following::div[@class='choose'][1]";
    }

    // actions
    @Step("Navigate to Products Page")
    public ProductsPage navigate() {
        try {
            page.navigate((PropertyReader.getProperty("baseUrlWeb") + productPage));
        } catch (TimeoutError e) {
            // ignore or just log
            LogsManager.error("TimeoutError ignored");
        }

        return this;
    }

    @Step("Search for product: {productName}")
    public ProductsPage searchProduct(String productName) {
        page.getByPlaceholder(searchField).fill(productName);
        page.locator(searchButton).click();
        return this;
    }

    @Step("Click on Add to Cart for product: {productName}")
    public ProductsPage clickOnAddToCart(String productName) {

        Locator productCard = page.locator(hoverOnProduct(productName));
        Locator addButton = page.locator(addToCartButton(productName));

        //productCard.scrollIntoViewIfNeeded();
        page.waitForTimeout(1000);
        productCard.hover();
        page.waitForTimeout(1000);
        addButton.click();
        return this;
    }

    @Step("Click on View Product for product: {productName}")
    public ProductDetailsPage clickOnViewProduct(String productName) {
        page.locator(viewProduct(productName)).click();
        return new ProductDetailsPage(page);
    }

    @Step("Click on View Cart")
    public CartPage clickOnViewCart() {
        page.locator(viewCartButton).click();
        return new CartPage(page);
    }

    @Step("Click on Continue Shopping")
    public ProductsPage clickOnContinueShopping() {
        page.locator(continueShoppingButton).click();
        return this;
    }

    // validations
    @Step("Validate product details for {productName} with price {productPrice}")
    public ProductsPage validateProductDetails(String productName, String productPrice) {
        page.locator(hoverOnProduct(productName)).hover();
        String actualProductName = page.locator(this.productName(productName)).innerText();
        page.locator(hoverOnProduct(productName)).hover();
        String actualProductPrice = page.locator(this.productPrice(productName)).innerText();
        LogsManager.info("Validating product details for: " + actualProductName, " with price: " + actualProductPrice);

        Assert.assertEquals(actualProductName, productName, "Product name does not match");
        Assert.assertEquals(actualProductPrice, productPrice, "Product price does not match");
        return this;
    }

    @Step("Validate item added label contains: {expectedText}")
    public ProductsPage validateProductAddedToCart(String expectedText) {
        String actualText = page.locator(itemAddedLabel).nth(0).innerText();
        LogsManager.info("Validating item added label. Actual: " + actualText + " | Expected: " + expectedText);
        Assert.assertEquals(actualText, expectedText, "Item added label does not match expected value.");
        return this;
    }

}
