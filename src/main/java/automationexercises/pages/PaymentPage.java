package automationexercises.pages;


import automationexercises.FileUtils;
import com.microsoft.playwright.Download;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;

import java.nio.file.Paths;

public class PaymentPage {


    private final Page page;

    public PaymentPage(Page page) {
        this.page = page;
    }


    //vars
    private String paymentEndpoint = "/payment";

    //locators
    private final String nameOnCard = "*[name=name_on_card]";
    private final String cardNumber = "*[name=card_number]";
    private final String cardCvc = "ex. 311";
    private final String cardMonthExpiration = "MM";
    private final String cardYearExpiration = "YYYY";
    private final String payButton = "#submit";
    private final String paymentSuccessMessage = "h2 > b";
    private final String downloadInvoiceButton = "//a[.='Download Invoice']";

    //actions
    @Step("Fill card info")
    public PaymentPage fillCardInfo(String nameOnCard, String cardNumber, String cardCvc, String cardMonthExpiration, String cardYearExpiration) {
        page.locator(this.nameOnCard).fill(nameOnCard);
        page.locator(this.cardNumber).fill(cardNumber);
        page.getByPlaceholder(this.cardCvc).fill(cardCvc);
        page.getByPlaceholder(this.cardMonthExpiration).fill(cardMonthExpiration);
        page.getByPlaceholder(this.cardYearExpiration).fill(cardYearExpiration);
        page.locator(payButton).click();
        return this;
    }

    @Step("Click on download invoice button")
    public PaymentPage clickOnDownloadInvoiceButton() {

        // Wait for the download to start
        Download download = page.waitForDownload(() -> {
            // Perform the action that initiates download
            page.locator(downloadInvoiceButton).click();
        });

        String userDir = System.getProperty("user.dir");
        String downloadPath = userDir + "\\src\\test\\resources\\downloads";

        download.saveAs(Paths.get(downloadPath ,"invoice.txt"));
        return this;
    }

    //validations
    @Step("Verify payment success message")
    public PaymentPage verifyPaymentSuccessMessage(String expectedMessage) {
        Assertions.assertEquals(expectedMessage, page.locator(paymentSuccessMessage).innerText(), "Payment success message is not matched");
        return this;
    }

    public PaymentPage verifyDownloadedFile(String invoiceName) {
        Assertions.assertTrue(FileUtils.isFileExists(invoiceName), "File is not existed");
        return this;
    }

}