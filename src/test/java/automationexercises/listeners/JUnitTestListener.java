package automationexercises.listeners;

import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.*;
import automationexercises.FileUtils;
import automationexercises.utils.dataReader.PropertyReader;
import automationexercises.utils.logs.LogsManager;
import automationexercises.utils.report.AllureAttachmentManager;
import automationexercises.utils.report.AllureConstants;
import automationexercises.utils.report.AllureEnvironmentManager;
import automationexercises.utils.report.AllureReportGenerator;
import automationexercises.utils.screenshots.ScreenshotManager;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * JUnit 5 Extension for managing test lifecycle events.
 * Equivalent to TestNG Listeners (IExecutionListener, IInvokedMethodListener,
 * ITestListener).
 * 
 * Handles:
 * - Test execution setup and teardown
 * - Directory management (cleaning and creating test output directories)
 * - Allure report integration
 * - Screenshot capture for test results
 * - Log attachment
 */
public class JUnitTestListener
        implements TestWatcher, BeforeAllCallback, AfterAllCallback, BeforeEachCallback, AfterEachCallback {

    private static final String PAGE_KEY = "playwright.page";
    private static final String START_TIME_KEY = "test.startTime";
    private static final String TEST_RESULT_KEY = "test.result";
    private static boolean executionStarted = false;

    // ==================== Test Execution Lifecycle ====================

    /**
     * Called before all tests in a class start (equivalent to TestNG's
     * onExecutionStart)
     * Note: This is called per test class, not for entire test suite
     */
    @Override
    public void beforeAll(ExtensionContext context) {
        // Only run setup once for the entire test execution
        if (!executionStarted) {
            executionStarted = true;
            LogsManager.info("Test Execution started");

            cleanTestOutputDirectories();
            LogsManager.info("Directories cleaned");

            createTestOutputDirectories();
            LogsManager.info("Directories created");

            PropertyReader.loadProperties();
            LogsManager.info("Properties loaded");

            AllureEnvironmentManager.setEnvironmentVariables();
            LogsManager.info("Allure environment set");
        }
    }

    /**
     * Called after all tests in a class finish (equivalent to TestNG's
     * onExecutionFinish)
     * Note: This is called per test class, not for entire test suite
     */
    @Override
    public void afterAll(ExtensionContext context) {
//        AllureReportGenerator.copyHistory();
//        AllureReportGenerator.generateReports(false);
//        AllureReportGenerator.generateReports(true);
//        AllureReportGenerator.openReport(AllureReportGenerator.renameReport());
        LogsManager.info("All tests in class completed");
    }

    // ==================== Test Method Lifecycle ====================

    /**
     * Called before each test method (equivalent to TestNG's beforeInvocation)
     */
    @Override
    public void beforeEach(ExtensionContext context) {
        String testName = context.getDisplayName();
        LogsManager.info("Test Case " + testName + " started");

        // Store test start time
        ExtensionContext.Store store = getStore(context);
        store.put(START_TIME_KEY, LocalDateTime.now());

        // Store Page reference if available from test instance
        try {
            Object testInstance = context.getTestInstance().orElse(null);
            if (testInstance != null) {
                // Try to get page field from test instance
                var pageField = testInstance.getClass().getSuperclass().getDeclaredField("page");
                pageField.setAccessible(true);
                Page page = (Page) pageField.get(testInstance);
                if (page != null) {
                    store.put(PAGE_KEY, page);
                }
            }
        } catch (Exception e) {
            LogsManager.warn("Could not access Page object: " + e.getMessage());
        }
    }

    /**
     * Called after each test method (equivalent to TestNG's afterInvocation)
     */
    @Override
    public void afterEach(ExtensionContext context) {
        String testName = context.getDisplayName();
        ExtensionContext.Store store = getStore(context);

        // Attach logs to Allure
        AllureAttachmentManager.attachLogs();

        // Add test execution metadata
        LocalDateTime startTime = store.get(START_TIME_KEY, LocalDateTime.class);
        if (startTime != null) {
            LocalDateTime endTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
           // Allure.parameter("Listener Start Time", startTime.format(formatter));
            //Allure.parameter("Listener End Time", endTime.format(formatter));
        }

        LogsManager.info("Test Case " + testName + " completed");
    }

    // ==================== Test Result Callbacks ====================

    /**
     * Called when a test succeeds (equivalent to TestNG's onTestSuccess)
     */
    @Override
    public void testSuccessful(ExtensionContext context) {
        String testName = context.getDisplayName();
        LogsManager.info("Test Case " + testName + " passed");

        // Capture screenshot
        ExtensionContext.Store store = getStore(context);
        Page page = store.get(PAGE_KEY, Page.class);
        if (page != null) {

            ScreenshotManager.captureScreenshotFull(page, "passed-" + testName);
            LogsManager.error("ScreenShot captured for passed test: " + testName);


        }
    }

    /**
     * Called when a test fails (equivalent to TestNG's onTestFailure)
     */
    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        String testName = context.getDisplayName();
        LogsManager.info("Test Case " + testName + " failed");
        LogsManager.error("Failure reason: " + cause.getMessage());

        // Capture screenshot
        ExtensionContext.Store store = getStore(context);
        Page page = store.get(PAGE_KEY, Page.class);
        if (page != null) {
            ScreenshotManager.captureScreenshotFull(page, "failed-" + testName);
        }
    }

    /**
     * Called when a test is skipped/aborted (equivalent to TestNG's onTestSkipped)
     */
    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        String testName = context.getDisplayName();
        LogsManager.info("Test Case " + testName + " skipped/aborted");

        // Capture screenshot
        ExtensionContext.Store store = getStore(context);
        Page page = store.get(PAGE_KEY, Page.class);
        if (page != null) {
            ScreenshotManager.captureScreenshotFull(page, "skipped-" + testName);
        }
    }

    /**
     * Called when a test is disabled
     */
    @Override
    public void testDisabled(ExtensionContext context, java.util.Optional<String> reason) {
        String testName = context.getDisplayName();
        LogsManager.info("Test Case " + testName + " disabled. Reason: " + reason.orElse("No reason provided"));
    }

    // ==================== Helper Methods ====================

    /**
     * Get the ExtensionContext store for this extension
     */
    private ExtensionContext.Store getStore(ExtensionContext context) {
        return context.getStore(ExtensionContext.Namespace.create(JUnitTestListener.class, context.getUniqueId()));
    }

    /**
     * Clean test output directories
     */
    private void cleanTestOutputDirectories() {
        try {
            // Clean allure-results
            FileUtils.cleanDirectory(AllureConstants.RESULTS_FOLDER.toFile());

            // Clean screenshots
            FileUtils.cleanDirectory(new File(ScreenshotManager.SCREENSHOTS_PATH));

            // Clean logs
            FileUtils.forceDelete(new File(LogsManager.LOGS_PATH + "logs.log"));
        } catch (Exception e) {
            LogsManager.error("Error cleaning directories: " + e.getMessage());
        }
    }

    /**
     * Create test output directories
     */
    private void createTestOutputDirectories() {
        try {
            // Create screenshots directory
            FileUtils.createDirectory(ScreenshotManager.SCREENSHOTS_PATH);

            // Create logs directory
            FileUtils.createDirectory("test-output" + File.separator + "Logs");
        } catch (Exception e) {
            LogsManager.error("Error creating directories: " + e.getMessage());
        }
    }

    /**
     * Register a shutdown hook to generate Allure reports after all tests complete
     */
    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                LogsManager.info("Generating Allure reports...");
                AllureReportGenerator.copyHistory();
                AllureReportGenerator.generateReports(false); // Full report
                AllureReportGenerator.generateReports(true); // Single-file report
                AllureReportGenerator.openReport(AllureReportGenerator.renameReport());
                LogsManager.info("Test Execution Finished");
            } catch (Exception e) {
                System.err.println("Error generating Allure reports: " + e.getMessage());
            }
        }));
    }
}
