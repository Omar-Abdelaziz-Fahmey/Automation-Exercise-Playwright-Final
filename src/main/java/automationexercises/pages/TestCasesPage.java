package automationexercises.pages;

import automationexercises.utils.dataReader.PropertyReader;
import automationexercises.utils.logs.LogsManager;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitUntilState;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;

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
        page.navigate(PropertyReader.getProperty("baseUrlWeb") + testCasesEndpoint,
                new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        return this;
    }

    // validations
    @Step("Validate Test Cases page is displayed")
    public TestCasesPage validateTestCasesPageIsDisplayed() {
        LogsManager.info("Validating Test Cases page is displayed");
        Assertions.assertTrue(page.locator(testCasesLabel).isVisible());
        return this;
    }

}
