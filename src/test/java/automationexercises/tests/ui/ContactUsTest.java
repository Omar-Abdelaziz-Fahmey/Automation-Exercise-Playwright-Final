package automationexercises.tests.ui;

import automationexercises.base.BaseTest;
import automationexercises.pages.ContactUsPage;
import automationexercises.pages.components.NavigationBarComponent;
import automationexercises.utils.dataReader.JsonReader;
import io.qameta.allure.*;
import org.testng.annotations.Test;

@Epic("Automation Exercise")
@Feature("UI Contact Us Management")
@Owner("Omar")
public class ContactUsTest extends BaseTest {
        JsonReader testData = new JsonReader("contactus-data");

        @Test(groups = { "contactus", "regression" })
        @Story("Contact Us Page Navigation - Happy Path")
        @Description("Verify that users can navigate to the Contact Us page from the navigation bar")
        @Severity(SeverityLevel.NORMAL)
        public void navigateToContactUsPageTest() {
                new NavigationBarComponent(page)
                                .clickOnContactUsButton()
                                .verifyContactUsPageIsDisplayed();
        }

        @Test(groups = { "contactus", "regression" })
        @Story("Submit Contact Us Form without Attachment - Happy Path")
        @Description("Verify that users can successfully submit the Contact Us form with valid data without attachment")
        @Severity(SeverityLevel.CRITICAL)
        public void submitContactUsFormWithValidDataWithoutAttachmentTest() {
                // Test implementation goes
                new ContactUsPage(page).navigate()
                                .fillContactUsForm(
                                                testData.getJsonData("contacts.name"),
                                                testData.getJsonData("contacts.email"),
                                                testData.getJsonData("contacts.subject"),
                                                testData.getJsonData("contacts.message"))
                                .verifyAlertMessageAfterSubmit(testData.getJsonData("messages.alertMessage"))
                                .clickSubmitButton()
                                .verifySuccessMessageAfterSubmit(testData.getJsonData("messages.successMessage"));

        }

        @Test(groups = { "contactus", "regression" })
        @Story("Submit Contact Us Form with Attachment - Happy Path")
        @Description("Verify that users can successfully submit the Contact Us form with valid data and an attachment")
        @Severity(SeverityLevel.CRITICAL)
        public void submitContactUsFormWithAttachmentTest() {
                // Test implementation goes here
                new ContactUsPage(page).navigate()
                                .fillContactUsForm(
                                                testData.getJsonData("contacts.name"),
                                                testData.getJsonData("contacts.email"),
                                                testData.getJsonData("contacts.subject"),
                                                testData.getJsonData("contacts.message"),
                                                testData.getJsonData("contacts.filePath"))
                                .verifyAlertMessageAfterSubmit(testData.getJsonData("messages.alertMessage"))
                                .clickSubmitButton()
                                .verifySuccessMessageAfterSubmit(testData.getJsonData("messages.successMessage"));

        }

        @Test(groups = { "contactus", "regression" })
        @Story("Submit Contact Us Form with Empty Email - Negative")
        @Description("Verify that users cannot submit the Contact Us form with an empty Email field")
        @Severity(SeverityLevel.NORMAL)
        public void submitContactUsFormWithEmptyEmailTest() {
                new ContactUsPage(page).navigate()
                                .fillContactUsForm(
                                                testData.getJsonData("contacts.name"),
                                                "",
                                                testData.getJsonData("contacts.subject"),
                                                testData.getJsonData("contacts.message"))
                                .clickSubmitButton()
                                .verifyEmailFieldValidationMessage();
        }

}
