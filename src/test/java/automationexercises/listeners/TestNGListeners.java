package automationexercises.listeners;

import automationexercises.FileUtils;

import automationexercises.utils.dataReader.PropertyReader;
import automationexercises.utils.logs.LogsManager;
import automationexercises.utils.report.AllureAttachmentManager;
import automationexercises.utils.report.AllureConstants;
import automationexercises.utils.report.AllureEnvironmentManager;
import automationexercises.utils.report.AllureReportGenerator;
import automationexercises.utils.screenshots.ScreenshotManager;
import automationexercises.base.BaseTest;

import com.microsoft.playwright.Page;
import org.testng.*;

import java.io.File;

public class TestNGListeners implements ISuiteListener, IExecutionListener, IInvokedMethodListener, ITestListener {
    public void onStart(ISuite suite) {
        suite.getXmlSuite().setName("Automation Exercise");
    }

    public void onExecutionStart() {
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

    public void onExecutionFinish() {
        AllureReportGenerator.copyHistory();
        AllureReportGenerator.generateReports(false);
        AllureReportGenerator.generateReports(true);
        AllureReportGenerator.openReport(AllureReportGenerator.renameReport());
        LogsManager.info("Test Execution Finished");
    }

    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        if (method.isTestMethod()) {
            LogsManager.info("Test Case " + testResult.getName() + " started");
        }
    }

    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        Page page = null;

        if (method.isTestMethod()) {
            Object currentClass = testResult.getInstance();
            if (currentClass instanceof BaseTest) {
                page = ((BaseTest) currentClass).getPage();
            }

            if (page != null) {
                switch (testResult.getStatus()) {
                    case ITestResult.SUCCESS ->
                            ScreenshotManager.captureScreenshotFull(page, "passed-" + testResult.getName());
                    case ITestResult.FAILURE ->
                            ScreenshotManager.captureScreenshotFull(page, "failed-" + testResult.getName());
                    case ITestResult.SKIP ->
                            ScreenshotManager.captureScreenshotFull(page, "skipped-" + testResult.getName());
                }
            }

            // Validation.assertAll(testResult); // Not needed for TestNG hard assertions,
            // usually used for soft asserts
            AllureAttachmentManager.attachRecords(testResult.getName());
            AllureAttachmentManager.attachLogs();

        }
    }

    public void onTestSuccess(ITestResult result) {
        LogsManager.info("Test Case " + result.getName() + " passed");
    }

    public void onTestFailure(ITestResult result) {
        LogsManager.info("Test Case " + result.getName() + " failed");
    }

    public void onTestSkipped(ITestResult result) {
        LogsManager.info("Test Case " + result.getName() + " skipped");
    }

    // cleaning and creating dirs (logs, screenshots, allure-results)
    private void cleanTestOutputDirectories() {
        // Implement logic to clean test output directories
        FileUtils.cleanDirectory(AllureConstants.RESULTS_FOLDER.toFile());
        FileUtils.cleanDirectory(new File(ScreenshotManager.SCREENSHOTS_PATH));
        FileUtils.cleanDirectory(new File("src/test/resources/downloads/"));
        // Log file cleanup is now handled by Log4j2 RollingFileAppender with
        // OnStartupTriggeringPolicy
    }

    private void createTestOutputDirectories() {
        // Implement logic to create test output directories
        FileUtils.createDirectory(ScreenshotManager.SCREENSHOTS_PATH);
        FileUtils.createDirectory("src/test/resources/downloads/");
    }
}