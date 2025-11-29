package automationexercises.base;

import com.microsoft.playwright.Page;
//import io.qameta.allure.Allure;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import automationexercises.listeners.JUnitTestListener;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import io.qameta.allure.junit5.AllureJunit5;

@ExtendWith({ AllureJunit5.class, JUnitTestListener.class })
public class BaseTest {
    protected Page page;

    @BeforeAll
    @Step("Initialize Playwright browser")
    public static void setUpAll() {
        PlaywrightManager.start();
    }

    @BeforeEach
    @Step("Create new page context for test")
    public void setUp(TestInfo testInfo) {
        page = PlaywrightManager.getPage();

        // Add test metadata
        String testName = testInfo.getDisplayName();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // Allure.parameter("Test Name", testName);
        // Allure.parameter("Test Start Time", timestamp);
        // Allure.parameter("Test Class",
        // testInfo.getTestClass().map(Class::getName).orElse("Unknown"));
        // Allure.parameter("Test Method", testInfo.getTestMethod().map(m ->
        // m.getName()).orElse("Unknown"));
    }

    @AfterEach
    @Step("Close page and capture final state")
    public void tearDown(TestInfo testInfo) {
        if (page != null) {
            // Capture screenshot before closing (useful for both passed and failed tests)
            try {
                byte[] screenshot = page.screenshot();
                String screenshotName = testInfo.getDisplayName() + " - Final State";
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

    @AfterAll
    @Step("Stop Playwright and cleanup resources")
    public static void tearDownAll() {
        PlaywrightManager.stop();
    }
}
