package automation.tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

import java.io.IOException;
import java.time.Duration;

public class LoginAPITest extends BaseTest
{
	private static final String BASE_URL = "https://qa.aerfinity.acumenaviation.in"; // Update if needed
    private static final String LOGIN_ENDPOINT = "/api/auth/login"; // Replace with the correct endpoint

    @Test
    public void testValidCredentials()
    {
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\"username\": \"support@acumen.aero\", \"password\": \"Acumen@123\"}")
                .post(BASE_URL + LOGIN_ENDPOINT);

        System.out.println("Response Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.prettyPrint()); // For a better response view

        Assert.assertEquals(response.getStatusCode(), 200, "Valid credentials test failed!");
    }

    @Test
    public void testInvalidCredentials()
    {
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\"username\": \"wrong@acumen.aero\", \"password\": \"wrongpassword\"}")
                .post(BASE_URL + LOGIN_ENDPOINT);

        System.out.println("Response Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.prettyPrint()); // For a better response view

        Assert.assertEquals(response.getStatusCode(), 401, "Invalid credentials test failed!");
    }
}