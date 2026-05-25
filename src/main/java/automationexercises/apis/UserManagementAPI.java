package automationexercises.apis;

import automationexercises.PlaywrightManager;
import automationexercises.utils.logs.LogsManager;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.FormData;
import com.microsoft.playwright.options.RequestOptions;
import io.qameta.allure.Step;
import org.testng.Assert;

public class UserManagementAPI {

    private APIRequestContext apiContext;
    private APIResponse response;
    private JsonObject responseJson;

    public UserManagementAPI() {
        apiContext= PlaywrightManager.getApiContext();
    }

    private static final String createAccount_endpoint = "/api/createAccount";
    private static final String deleteAccount_endpoint = "/api/deleteAccount";


    private void parseResponse() {
        String body = response.text();
        LogsManager.info(body);
        responseJson = JsonParser.parseString(body).getAsJsonObject();
    }


    @Step("Create a new user account with full details")
    public UserManagementAPI createRegisterUserAccount(
            String name, String email, String pass, String title,
            String birth_date, String birth_month, String birth_year,
            String firstName, String lastName, String company,
            String address1, String address2, String country,
            String zipcode, String state, String city,
            String mobile_number) {


        FormData formData = FormData.create()
                .set("name", name)
                .set("email", email)
                .set("password", pass)
                .set("title", title)
                .set("birth_date", birth_date)
                .set("birth_month", birth_month)
                .set("birth_year", birth_year)
                .set("firstname", firstName)
                .set("lastname", lastName)
                .set("company", company)
                .set("address1", address1)
                .set("address2", address2)
                .set("country", country)
                .set("zipcode", zipcode)
                .set("state", state)
                .set("city", city)
                .set("mobile_number", mobile_number);


        response = apiContext.post(createAccount_endpoint,
                RequestOptions.create()
                        .setForm(formData)
        );

        parseResponse();
        return this;
    }


    @Step("Create a new user account with minimal details")
    public UserManagementAPI createRegisterUserAccount(
            String name, String email, String pass,
            String firstName, String lastName) {

        FormData formData = FormData.create()
                .set("name", name)
                .set("email", email)
                .set("password", pass)
                .set("title", "Mr")
                .set("birth_date", "1")
                .set("birth_month", "January")
                .set("birth_year", "1990")
                .set("firstname", firstName)
                .set("lastname", lastName)
                .set("company", "company")
                .set("address1", "address1")
                .set("address2", "address2")
                .set("country", "India")
                .set("zipcode", "123456")
                .set("state", "state")
                .set("city", "city")
                .set("mobile_number", "1234567890");

        response = apiContext.post(createAccount_endpoint,
                RequestOptions.create()
                        .setForm(formData)
        );

        parseResponse();
        return this;
    }

    @Step("Delete user account by email")
    public UserManagementAPI deleteUserAccount(String email, String password) {

        FormData formData = FormData.create()
                .set("email", email)
                .set("password", password);

        response = apiContext.delete(deleteAccount_endpoint,
                RequestOptions.create()
                        .setForm(formData)
        );

        parseResponse();
        return this;
    }


    @Step("Verify that user is created successfully")
    public UserManagementAPI verifyUserCreatedSuccessfully() {

        Assert.assertEquals(
                responseJson.get("message").getAsString(),
                "User created!",
                "User is not created successfully"
        );
        return this;
    }

    @Step("Verify that user is deleted successfully")
    public UserManagementAPI verifyUserDeletedSuccessfully() {
        Assert.assertEquals(
                responseJson.get("message").getAsString(),
                "Account deleted!",
                "User is not deleted successfully"
        );
        return this;
    }

    @Step("Verify that user is deleted while the account should not exist")
    public UserManagementAPI verifyUserDeletedSuccessfullyWhileAccountShouldNotExist() {
        Assert.assertNotEquals(
                responseJson.get("message").getAsString(),
                "Account deleted!",
                "User is deleted successfully while the account should not exist"
        );
        return this;
    }
}