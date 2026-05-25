package automationexercises;

import automationexercises.utils.dataReader.PropertyReader;
import automationexercises.utils.logs.LogsManager;
import com.microsoft.playwright.*;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class PlaywrightManager {
    private static Playwright playwright;
    private static Browser browser;
    private static BrowserContext context;
    private static Page page;
    private static APIRequestContext apiContext;

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

            // tracer
            context.tracing().start(new Tracing.StartOptions()
                    .setScreenshots(true)
                    .setSnapshots(true)
                    .setSources(true));

            context.route("**/*", route -> {
                String url = route.request().url().toLowerCase();
                if (url.contains("googleads") ||
                        url.contains("googlesyndication") ||
                        url.contains("doubleclick") ||
                        url.contains("googletagservices") ||
                        url.contains("adservice.google") ||
                        url.contains("googleadservices") ||
                        url.contains("ads.google") ||
                        url.contains("securepubads") ||
                        url.contains("adnxs") ||
                        url.contains("outbrain") ||
                        url.contains("taboola")) {
                    route.abort();
                } else {
                    route.resume();
                }
            });

            page = context.newPage();

            try {
                page.navigate((PropertyReader.getProperty("baseUrlWeb")));
            } catch (TimeoutError e) {
                LogsManager.error("TimeoutError ignored");
            }
        }
        return page;
    }

    public static void closePage() {
        context.tracing().stop(new Tracing.StopOptions()
                .setPath(Paths.get("trace.zip")));
        if (page != null) {
            page.close();
            page = null;
        }
        if (context != null) {
            context.close();
            context = null;
        }
    }


    public static void closeApiContext() {
        if (apiContext != null) {
            apiContext.dispose();
        }
    }

    public static APIRequestContext getApiContext() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("User-Agent", "PlaywrightAPI/1.0");

        apiContext = playwright.request().newContext(
                new APIRequest.NewContextOptions()
                        .setBaseURL(PropertyReader.getProperty("baseUrlWeb"))
                        .setExtraHTTPHeaders(headers)
        );
        return apiContext;
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
