package automationexercises.pages.components;


import automationexercises.utils.logs.LogsManager;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import org.testng.Assert;

public class SubscriptionComponent {

    private final Page page;

    public SubscriptionComponent(Page driver) {
        this.page = driver;
    }

    //locators
    private final String subscriptionInput = "#susbscribe_email";
    private final String subscribeButton = "#subscribe";
    private final String subscriptionSuccessMessage = ".alert-success.alert";

    //actions

    @Step("Enter email for subscription: {email}")
    public SubscriptionComponent enterSubscriptionEmail(String email) {
        page.locator(subscriptionInput).fill(email);
        page.locator(subscribeButton).click();
        return this;
    }

    //validations
    @Step("Validate subscription success message is displayed: {expectedMessage}")
    public SubscriptionComponent validateSubscriptionSuccessMessage(String expectedMessage) {
        String actualMessage = page.locator(subscriptionSuccessMessage).innerText();
        LogsManager.info("Validating subscription success message. Expected: " + expectedMessage + ", Actual: " + actualMessage);
        Assert.assertEquals(actualMessage, expectedMessage, "Subscription success message does not match!");
        return this;
    }


}
