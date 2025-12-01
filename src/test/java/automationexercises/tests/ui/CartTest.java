package automationexercises.tests.ui;

import automationexercises.base.BaseTest;
import automationexercises.pages.ProductsPage;
import automationexercises.utils.dataReader.JsonReader;
import io.qameta.allure.*;
import org.testng.annotations.Test;

@Epic("Automation Exercise")
@Feature("UI Product Management")
@Story("Cart Management")
@Owner("Omar")
public class CartTest extends BaseTest {
        JsonReader testData = new JsonReader("cart-data");

        @Test(groups = { "cart", "regression" })
        @Story("Cart Management")
        @Description("Verify product details on cart without login")
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

        @Test(groups = { "cart", "regression" })
        @Story("Cart Management")
        @Description("Verify product quantity in cart")
        @Severity(SeverityLevel.CRITICAL)
        public void verifyProductQuantityInCartTC() {
                new ProductsPage(page).navigationBar.clickProductsButton()
                                .clickOnViewProduct(testData.getJsonData("product2.name"))
                                .setProductQuantity(testData.getJsonData("product2.quantity"))
                                .clickOnAddToCart()
                                .validateProductAddedToCart(testData.getJsonData("messages.cartAdded"))
                                .clickOnViewCart()
                                .verifyProductDetailsOnCart(
                                                testData.getJsonData("product2.name"),
                                                testData.getJsonData("product2.price"),
                                                testData.getJsonData("product2.quantity"),
                                                testData.getJsonData("product2.total"));

        }

}
