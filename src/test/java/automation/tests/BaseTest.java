package automation.tests;

import java.io.IOException;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;

import automation.BaseMethods.Controls;
import automation.ExtentReport.ReportsOriginal;
import automation.PageObject.PageObject_Login;

public class BaseTest {
    protected WebDriver driver;
    protected PageObject_Login pl;
    protected ReportsOriginal reports;

    @BeforeTest
    public void setup() throws IOException, InterruptedException
    {
        Controls.setpropertyfile();
        Controls.startUP();
        driver = Controls.driver;
        reports = ReportsOriginal.getInstance();
        pl = new PageObject_Login(driver, Controls.loginprop);
    }

    public void login() throws IOException, InterruptedException
    {
        reports.startTest("Login Test");
        try
        {
            String useremail = Controls.loginprop.getProperty("user");
            String password = Controls.loginprop.getProperty("pwd");
            pl.login(useremail, password);

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement success = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Controls.loginprop.getProperty("login_success"))));
            Assert.assertNotNull(success, "Login failed.");
            reports.log(com.aventstack.extentreports.Status.PASS, "Successful login");
        }
        catch(Exception e)
        {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@id,'notistack-snackbar')]")));
            String errorMessage = error.getText();
            reports.log(com.aventstack.extentreports.Status.FAIL, "Login failed: " + errorMessage);
            throw new AssertionError("Login test failed: " + errorMessage, e);
        }
    }

    @AfterMethod
    public void afterMethod(ITestResult result)
    {
    	try
    	{
            reports.startTest(result.getMethod().getMethodName());
            switch (result.getStatus())
            {
                case ITestResult.FAILURE:
                    reports.log(com.aventstack.extentreports.Status.FAIL,
                            result.getMethod().getMethodName() + ": Test failed");
                    break;
                case ITestResult.SUCCESS:
                    reports.log(com.aventstack.extentreports.Status.PASS,
                            result.getMethod().getMethodName() + ": Test passed");
                    break;
                case ITestResult.SKIP:
                    String skipMessage = result.getThrowable() != null
                            ? result.getMethod().getMethodName()
                            : "Test skipped.";
                    reports.log(com.aventstack.extentreports.Status.SKIP, skipMessage);
                    break;
                default:
                    reports.log(com.aventstack.extentreports.Status.WARNING, "Unknown test result status.");
                    break;
            }
        }
    	finally
    	{
            reports.endTest();
        }
    }

    @AfterTest
    public void afterTest()
    {
        if(Controls.driver != null)
        {
            reports.openReport(Controls.driver);
        }
    }
}