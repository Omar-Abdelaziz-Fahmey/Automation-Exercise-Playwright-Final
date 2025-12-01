package automationexercises.tests.ui;

import automationexercises.base.BaseTest;
import automationexercises.pages.components.NavigationBarComponent;
import io.qameta.allure.*;
import org.testng.annotations.Test;

@Epic("Automation Exercise")
@Feature("UI Test Cases Management")
@Story("Test Cases Management")
@Severity(SeverityLevel.MINOR)
@Owner("Omar")
public class TestCasesTest extends BaseTest {

    @Test(groups = { "testcases", "regression" })
    @Description("Navigate to Test Cases page and validate it is displayed")
    public void navigateToTestCasesPageTest() {
        new NavigationBarComponent(page).navigate()
                .clickOnTestCasesButton()
                .validateTestCasesPageIsDisplayed();
    }

}
