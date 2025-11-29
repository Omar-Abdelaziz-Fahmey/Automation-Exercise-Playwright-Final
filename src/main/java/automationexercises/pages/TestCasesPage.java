package automationexercises.pages;


import automationexercises.utils.actions.PageActions;
import automationexercises.utils.dataReader.PropertyReader;
import automationexercises.utils.logs.LogsManager;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;

public class TestCasesPage {


    private final Page page;
    private final PageActions pageActions;

    public TestCasesPage(Page page) {
        this.page = page;
        this.pageActions = new PageActions(page);
    }

    //variables
    String testCasesEndpoint = "/test_cases";


    //locators
    String testCasesLabel = "div>h2>b";

    //actions
    @Step("Navigate to Test Cases page")
    public TestCasesPage navigate() {
        page.navigate(PropertyReader.getProperty("baseUrlWeb") + testCasesEndpoint);
        return this;
    }

    //validations
    @Step("Validate Test Cases page is displayed")
    public TestCasesPage validateTestCasesPageIsDisplayed() {
        LogsManager.info("Validating Test Cases page is displayed");
        Assertions.assertTrue(pageActions.find(testCasesLabel).isVisible());
        return this;
    }

}
