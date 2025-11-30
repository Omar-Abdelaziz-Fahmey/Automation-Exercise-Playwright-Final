package automationexercises.tests.ui;

import automationexercises.base.BaseTest;
import automationexercises.pages.ProductsPage;
import automationexercises.utils.dataReader.JsonReader;
import io.qameta.allure.*;
import org.junit.jupiter.api.Test;


@Epic("Automation Exercise")
@Feature("UI User Management")
@Story("Product Details")
@Severity(SeverityLevel.CRITICAL)
@Owner("Omar")
public class ProductDetailsTest extends BaseTest {
    JsonReader testData = new JsonReader("products-details-data");
    @Test
    public void verifyProductDetailsWithoutLoginTC() {
        new ProductsPage(page).navigate()
                .clickOnViewProduct(testData.getJsonData("product.name"))
                .verifyProductDetails(
                        testData.getJsonData("product.name"),
                        testData.getJsonData("product.price")
                );
    }

    @Test
    public void verifyReviewMessageWithoutLoginTC() {
        new ProductsPage(page).navigate()
                .clickOnViewProduct(testData.getJsonData("product.name"))
                .addReview(
                        testData.getJsonData("review.name")
                        ,testData.getJsonData("review.email")
                        ,testData.getJsonData("review.review")
                )
                .verifyReviewMsg(testData.getJsonData("messages.review"));
    }


}
