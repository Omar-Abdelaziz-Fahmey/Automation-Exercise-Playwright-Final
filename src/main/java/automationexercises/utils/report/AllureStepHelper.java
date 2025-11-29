//package swaglabs.utils.report;
//
//import io.qameta.allure.Allure;
//import io.qameta.allure.Step;
//
///**
// * Utility class for enhanced Allure reporting with custom steps and
// * attachments.
// * Provides convenient methods for logging, adding parameters, and attaching
// * content to Allure reports.
// */
//public class AllureStepHelper {
//
//    /**
//     * Log an informational message to the Allure report
//     *
//     * @param message The information message to log
//     */
//    @Step("ℹ️ Info: {message}")
//    public static void logInfo(String message) {
//        Allure.addAttachment("Info", "text/plain", message, ".txt");
//    }
//
//    /**
//     * Log a warning message to the Allure report
//     *
//     * @param message The warning message to log
//     */
//    @Step("⚠️ Warning: {message}")
//    public static void logWarning(String message) {
//        Allure.addAttachment("Warning", "text/plain", message, ".txt");
//    }
//
//    /**
//     * Log an error message with optional throwable to the Allure report
//     *
//     * @param message   The error message to log
//     * @param throwable The exception/error that occurred (can be null)
//     */
//    @Step("❌ Error: {message}")
//    public static void logError(String message, Throwable throwable) {
//        StringBuilder errorDetails = new StringBuilder(message);
//        if (throwable != null) {
//            errorDetails.append("\n\nException: ").append(throwable.getClass().getName());
//            errorDetails.append("\nMessage: ").append(throwable.getMessage());
//            errorDetails.append("\n\nStack Trace:\n");
//            for (StackTraceElement element : throwable.getStackTrace()) {
//                errorDetails.append("  at ").append(element.toString()).append("\n");
//            }
//        }
//        Allure.addAttachment("Error Details", "text/plain", errorDetails.toString(), ".txt");
//    }
//
//    /**
//     * Log an error message without a throwable
//     *
//     * @param message The error message to log
//     */
//    @Step("❌ Error: {message}")
//    public static void logError(String message) {
//        logError(message, null);
//    }
//
//    /**
//     * Add a parameter to the current Allure step
//     *
//     * @param name  The parameter name
//     * @param value The parameter value
//     */
//    public static void addParameter(String name, String value) {
//        Allure.parameter(name, value);
//    }
//
//    /**
//     * Add a text attachment to the Allure report
//     *
//     * @param name    The attachment name
//     * @param content The text content to attach
//     */
//    public static void addTextAttachment(String name, String content) {
//        Allure.addAttachment(name, "text/plain", content, ".txt");
//    }
//
//    /**
//     * Add a JSON attachment to the Allure report
//     *
//     * @param name        The attachment name
//     * @param jsonContent The JSON content to attach
//     */
//    public static void addJsonAttachment(String name, String jsonContent) {
//        Allure.addAttachment(name, "application/json", jsonContent, ".json");
//    }
//
//    /**
//     * Add an HTML attachment to the Allure report
//     *
//     * @param name        The attachment name
//     * @param htmlContent The HTML content to attach
//     */
//    public static void addHtmlAttachment(String name, String htmlContent) {
//        Allure.addAttachment(name, "text/html", htmlContent, ".html");
//    }
//
//    /**
//     * Add a custom step with a given name and description
//     *
//     * @param stepName        The name of the step
//     * @param stepDescription Description of what the step does
//     */
//    @Step("{stepName}")
//    public static void customStep(String stepName, String stepDescription) {
//        if (stepDescription != null && !stepDescription.isEmpty()) {
//            addTextAttachment("Step Details", stepDescription);
//        }
//    }
//
//    /**
//     * Mark a step as passed with a custom message
//     *
//     * @param message The success message
//     */
//    @Step("✅ Passed: {message}")
//    public static void stepPassed(String message) {
//        Allure.step(message);
//    }
//
//    /**
//     * Mark a step as failed with a custom message
//     *
//     * @param message The failure message
//     */
//    @Step("❌ Failed: {message}")
//    public static void stepFailed(String message) {
//        Allure.step(message);
//    }
//
//    /**
//     * Add multiple parameters at once
//     *
//     * @param parameters Pairs of parameter names and values (name1, value1, name2,
//     *                   value2, ...)
//     */
//    public static void addParameters(String... parameters) {
//        if (parameters.length % 2 != 0) {
//            throw new IllegalArgumentException("Parameters must be provided in name-value pairs");
//        }
//        for (int i = 0; i < parameters.length; i += 2) {
//            Allure.parameter(parameters[i], parameters[i + 1]);
//        }
//    }
//
//    /**
//     * Add a link to the Allure report (e.g., issue tracker, test management system)
//     *
//     * @param name The link name/label
//     * @param url  The URL to link to
//     */
//    public static void addLink(String name, String url) {
//        Allure.link(name, url);
//    }
//
//    /**
//     * Add an issue link to the Allure report
//     *
//     * @param issueId  The issue ID (e.g., JIRA-123)
//     * @param issueUrl The URL to the issue
//     */
//    public static void addIssue(String issueId, String issueUrl) {
//        Allure.issue(issueId, issueUrl);
//    }
//
//    /**
//     * Add a test management system link to the Allure report
//     *
//     * @param tmsId  The TMS ID
//     * @param tmsUrl The URL to the TMS
//     */
//    public static void addTmsLink(String tmsId, String tmsUrl) {
//        Allure.tms(tmsId, tmsUrl);
//    }
//}
