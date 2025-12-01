package automationexercises.base;

import automationexercises.PlaywrightManager;
import automationexercises.listeners.TestNGListeners;
import automationexercises.utils.dataReader.PropertyReader;
import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
@Listeners({ TestNGListeners.class })
public class BaseTest {
    protected Page page;

    public Page getPage() {
        return page;
    }

    @BeforeClass
    @Step("Initialize Playwright browser")
    public void setUpAll() {
        PropertyReader.loadProperties();
        PlaywrightManager.start();
    }

    @BeforeMethod
    @Step("Create new page context for test")
    public void setUp(Method method) {
        page = PlaywrightManager.getPage();
        String testName = method.getName();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    @AfterMethod
    @Step("Close page and capture final state")
    public void tearDown(ITestResult result) {
        if (page != null) {
            // Capture screenshot before closing (useful for both passed and failed tests)
            try {
                byte[] screenshot = page.screenshot();
                String screenshotName = result.getName() + " - Final State";
                Allure.addAttachment(screenshotName, "image/png",
                        new ByteArrayInputStream(screenshot), ".png");
            } catch (Exception e) {
                System.err.println("Failed to capture screenshot: " + e.getMessage());
            }

            // Add completion timestamp
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            // Allure.parameter("Test End Time", timestamp);

            PlaywrightManager.closePage();
        }
    }

    @AfterClass
    @Step("Stop Playwright and cleanup resources")
    public void tearDownAll() {
        // PlaywrightManager.closePage();
        PlaywrightManager.stop();
    }
}
