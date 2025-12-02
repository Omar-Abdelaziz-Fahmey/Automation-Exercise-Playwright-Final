package automationexercises.pages;

import automationexercises.utils.dataReader.PropertyReader;
import automationexercises.utils.logs.LogsManager;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.TimeoutError;
import com.microsoft.playwright.options.WaitUntilState;
import io.qameta.allure.Step;
import org.testng.Assert;

public class TestCasesPage {

    private final Page page;

    public TestCasesPage(Page page) {
        this.page = page;
    }

    // variables
    String testCasesEndpoint = "/test_cases";

    // locators
    String testCasesLabel = "div>h2>b";

    // actions
    @Step("Navigate to Test Cases page")
    public TestCasesPage navigate() {

        try {
            page.navigate((PropertyReader.getProperty("baseUrlWeb") + testCasesEndpoint));
        } catch (TimeoutError e) {
            // ignore or just log
            LogsManager.error("TimeoutError ignored");
        }
        return this;
    }

    // validations
    @Step("Validate Test Cases page is displayed")
    public TestCasesPage validateTestCasesPageIsDisplayed() {
        LogsManager.info("Validating Test Cases page is displayed");
        Assert.assertTrue(page.locator(testCasesLabel).isVisible());
        return this;
    }

}
