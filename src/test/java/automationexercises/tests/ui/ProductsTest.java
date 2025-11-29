package automationexercises.tests.ui;

import automationexercises.base.BaseTest;
import automationexercises.pages.ProductsPage;
import automationexercises.utils.TimeManager;
import automationexercises.utils.dataReader.JsonReader;
import io.qameta.allure.*;
import org.junit.jupiter.api.Test;


@Epic("Automation Exercise")
@Feature("UI Product Management")
@Story("Product Management")
@Severity(SeverityLevel.CRITICAL)
@Owner("Omar")
public class ProductsTest extends BaseTest {
    String timestamp = TimeManager.getSimpleTimestamp();
    JsonReader testData = new JsonReader("products-data");

    @Test
    @Description("Search for a product and validate its details")
    public void searchForProductAndValidateDetailsTest() {
        new ProductsPage(page).navigate()
                .searchProduct(testData.getJsonData("searchedProduct.name"))
                .validateProductDetails(
                        testData.getJsonData("searchedProduct.name"),
                        testData.getJsonData("searchedProduct.price")
                );
    }
    @Test
    @Description("Add product to cart without logging in")
    public void addToCartWithoutLoggingInTest() {
        new ProductsPage(page).navigate()
                .clickOnAddToCart(testData.getJsonData("product1.name"))
                .validateProductAddedToCart(
                        testData.getJsonData("messages.cartAdded")
                );
    }

}
