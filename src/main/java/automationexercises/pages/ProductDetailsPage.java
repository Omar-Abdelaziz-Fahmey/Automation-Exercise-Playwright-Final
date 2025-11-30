package automationexercises.pages;


import automationexercises.utils.dataReader.PropertyReader;
import automationexercises.utils.logs.LogsManager;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;

public class ProductDetailsPage {

    private final Page page;

    public ProductDetailsPage(Page page) {
        this.page = page;
    }


    //vars
    private String productDetailsEndpoint = "/product-details/2";

    //locators
    private final String productName = "div .product-information>h2";
    private final String productPrice = "span >span";
    private final String name = "#name";
    private final String email = "#email";
    private final String reviewTextArea ="#review";
    private final String reviewButton = "#button-review";
    private final String reviewMsg = "#review-section span";

    private final String quantity = "#quantity";
    private final String addToCartButton = "*[type=button]";


    //actions
    @Step("Navigating to product details")
    public ProductDetailsPage navigate() {
        page.navigate(PropertyReader.getProperty("baseUrlWeb") + productDetailsEndpoint);
        return this;
    }

    @Step("write review on product")
    public ProductDetailsPage addReview(String name, String email, String review) {
        page.locator(this.name).fill(name);
        page.locator(this.email).fill(email);
        page.locator(this.reviewTextArea).fill(review);
        page.locator(reviewButton).click();
        return this;
    }

    @Step("set product quantity")
    public ProductDetailsPage setProductQuantity(String quantity) {
        page.locator(this.quantity).fill(quantity);

        return this;
    }

    @Step("click on add to cart")
    public ProductsPage clickOnAddToCart() {
        page.locator(addToCartButton).click();
        return new ProductsPage(page);
    }


    //validations
    @Step("verify product details")
    public ProductDetailsPage verifyProductDetails(String productName, String productPrice) {

        String actualProductName = page.locator(this.productName).innerText();
        String actualProductPrice = page.locator(this.productPrice).innerText();
        LogsManager.info("actual product name:", actualProductName, "actual price:", actualProductPrice);
        Assertions.assertEquals(productName, actualProductName, "Product Name Verification Failed");
        Assertions.assertEquals(productPrice, actualProductPrice, "Product Price Verification Failed");
        return this;
    }

    @Step("verify review message")
    public ProductDetailsPage verifyReviewMsg(String msg) {
        String actualReviewMsg = page.locator(reviewMsg).innerText();
        LogsManager.info("actual review message:", actualReviewMsg);
        Assertions.assertEquals(msg, actualReviewMsg, "Review Message Verification Failed");
        return this;
    }


}
