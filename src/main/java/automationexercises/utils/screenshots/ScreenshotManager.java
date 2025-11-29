package automationexercises.utils.screenshots;

import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;
import automationexercises.utils.logs.LogsManager;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utility class for capturing and managing screenshots with Playwright.
 * Provides methods to capture screenshots and attach them to Allure reports.
 */
public class ScreenshotManager {

    public static final String SCREENSHOTS_PATH = "test-output" + File.separator + "screenshots" + File.separator;

    private ScreenshotManager() {
        // Prevent instantiation
    }

    /**
     * Capture a screenshot using Playwright Page object
     *
     * @param page     The Playwright Page object
     * @param testName The name of the test (used for filename)
     * @return byte array of the screenshot, or null if capture fails
     */
    public static byte[] captureScreenshot(Page page, String testName) {
        if (page == null) {
            LogsManager.warn("Page is null, cannot capture screenshot for: " + testName);
            return null;
        }

        try {
            byte[] screenshot = page.screenshot();
            LogsManager.info("Screenshot captured for: " + testName);
            return screenshot;
        } catch (Exception e) {
            LogsManager.error("Failed to capture screenshot for: " + testName, e.getMessage());
            return null;
        }
    }

    /**
     * Capture screenshot and save to file
     *
     * @param page     The Playwright Page object
     * @param fileName The filename (without extension)
     */
    public static void captureAndSaveScreenshot(Page page, String fileName) {
        byte[] screenshot = captureScreenshot(page, fileName);
        if (screenshot != null) {
            saveScreenshotToFile(screenshot, fileName);
        }
    }

    /**
     * Save screenshot byte array to file
     *
     * @param screenshot The screenshot byte array
     * @param fileName   The filename (without extension)
     */
    public static void saveScreenshotToFile(byte[] screenshot, String fileName) {
        try {
            Path screenshotPath = Paths.get(SCREENSHOTS_PATH + fileName + ".png");
            Files.createDirectories(screenshotPath.getParent());
            Files.write(screenshotPath, screenshot);
            LogsManager.info("Screenshot saved to: " + screenshotPath);
        } catch (IOException e) {
            LogsManager.error("Failed to save screenshot: " + fileName, e.getMessage());
        }
    }

    /**
     * Attach screenshot to Allure report
     *
     * @param screenshot The screenshot byte array
     * @param name       The attachment name
     */
    public static void attachScreenshotToAllure(byte[] screenshot, String name) {
        if (screenshot != null) {
            try {
                Allure.addAttachment(name, "image/png",
                        new ByteArrayInputStream(screenshot), ".png");
                LogsManager.info("Screenshot attached to Allure: " + name);
            } catch (Exception e) {
                LogsManager.error("Failed to attach screenshot to Allure: " + name, e.getMessage());
            }
        }
    }

    /**
     * Capture screenshot and attach to Allure report
     *
     * @param page The Playwright Page object
     * @param name The attachment name
     */
    public static void captureAndAttachToAllure(Page page, String name) {
        byte[] screenshot = captureScreenshot(page, name);
        attachScreenshotToAllure(screenshot, name);
    }

    /**
     * Capture screenshot, save to file, and attach to Allure
     *
     * @param page     The Playwright Page object
     * @param testName The test name
     */
    public static void captureScreenshotFull(Page page, String testName) {
        byte[] screenshot = captureScreenshot(page, testName);
        if (screenshot != null) {
            saveScreenshotToFile(screenshot, testName);
            attachScreenshotToAllure(screenshot, testName);
        }
    }
}
