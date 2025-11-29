package automationexercises;

import automationexercises.utils.dataReader.PropertyReader;
import com.microsoft.playwright.*;

public class PlaywrightManager {
    private static Playwright playwright;
    private static Browser browser;
    private static BrowserContext context;
    private static Page page;

    public static void start() {
        if (playwright == null) {
            playwright = Playwright.create();
            BrowserType.LaunchOptions options = new BrowserType.LaunchOptions();
            if (PropertyReader.getProperty("executionType").equalsIgnoreCase("LocalHeadless")) {

                options.setHeadless(true);
            } else {
                options.setHeadless(false);
            }

            if (PropertyReader.getProperty("browserType").equalsIgnoreCase("chrome")) {
                options.setArgs(java.util.Arrays.asList("--start-maximized"));

                browser = playwright.chromium().launch(options);
            } else {
                browser = playwright.firefox().launch(options);
            }
        }
    }

    public static Page getPage() {
        if (page == null) {
            context = browser.newContext(new Browser.NewContextOptions().setViewportSize(null));
            page = context.newPage();
            page.navigate(PropertyReader.getProperty("baseUrlWeb"));
        }
        return page;
    }

    public static void closePage() {
        if (page != null) {
            page.close();
            page = null;
        }
        if (context != null) {
            context.close();
            context = null;
        }
    }

    public static void stop() {
        if (browser != null) {
            browser.close();
            browser = null;
        }
        if (playwright != null) {
            playwright.close();
            playwright = null;
        }
    }
}
