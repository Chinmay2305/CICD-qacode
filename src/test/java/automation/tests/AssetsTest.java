package automation.tests;

import org.testng.annotations.Test;
import java.io.IOException;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import automation.PageObject.PageObject_AirframeCashflow;
import automation.PageObject.PageObject_Assets;
import java.time.Duration;
import java.util.NoSuchElementException;
import java.util.Properties;
import org.apache.poi.EncryptedDocumentException;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import automation.BaseMethods.Controls;
import automation.ExtentReport.ReportsOriginal;
import automation.PageObject.PageObject_Assets;
import automation.PageObject.PageObject_Login;
import automation.PageObject.PageObject_Projects;

public class AssetsTest extends ProjectsTest
{
	private PageObject_Assets pa;
    private PageObject_Projects pp;
    private PageObject_AirframeCashflow pac;

	@BeforeTest
	public void setup() throws IOException, InterruptedException
	{
		super.setup();
		pa = new PageObject_Assets(driver);
		pp = new PageObject_Projects(driver);
		pac = new PageObject_AirframeCashflow(driver);
	}
	
	@Test(priority = 0)
	public void verifylogin() throws IOException, InterruptedException
	{
		login();
	}
	
	@Test(priority = 1)
	public void firstproject() throws Exception
	{	
		pp.openprojects();
		pp.open1stproject();
	}
	
	@Test(priority = 2, dependsOnMethods = {"firstproject"})
	public void harvestlist() throws InterruptedException, EncryptedDocumentException, IOException
	{
		pa.addasset();
		pa.confirmasset();
		pa.addharvestlist();
		pa.harvestlistpage();
	}
	
	@Test(priority = 3, dependsOnMethods = {"harvestlist"})
	public void inventorycheck() throws InterruptedException, EncryptedDocumentException, IOException, ElementClickInterceptedException, StaleElementReferenceException
	{
		pa.inventorycheckpage();
	}
	
	@Test(priority = 4, dependsOnMethods = {"inventorycheck"})
	public void pricing() throws InterruptedException, EncryptedDocumentException, IOException
	{
		pa.pricingpage();
	}
	
	@Test(priority = 5, dependsOnMethods = {"pricing"})
	public void cashflow() throws InterruptedException, EncryptedDocumentException, IOException
	{
		Thread.sleep(5000);
		pac.cashflowsetup();
		pac.cashflowview();
	}
	
	@AfterMethod
	public void afterMethod(ITestResult result)
	{
		super.afterMethod(result);
	}
	
	@AfterTest
	public void afterTest()
	{
		super.afterTest();
	}
}