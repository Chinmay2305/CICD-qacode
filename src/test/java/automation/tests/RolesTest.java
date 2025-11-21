package automation.tests;

import org.testng.annotations.Test;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputFilter.Config;
import java.time.Duration;
import java.util.NoSuchElementException;
import java.util.Properties;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import automation.BaseMethods.Controls;
import automation.ExtentReport.ReportsOriginal;
import automation.PageObject.PageObject_Assets;
import automation.PageObject.PageObject_Login;
import automation.PageObject.PageObject_Roles;
import automation.PageObject.PageObject_Users;
import io.opentelemetry.sdk.autoconfigure.spi.ConfigProperties;

public class RolesTest extends BaseTest
{
	private PageObject_Roles pr;

	@BeforeTest
	public void setup() throws IOException, InterruptedException
	{
		super.setup();
		pr = new PageObject_Roles(driver);
	}
	
	@Test(priority = 0)
	public void verifylogin() throws Exception
	{
		login();
	}

	@Test(priority = 1, dependsOnMethods = {"verifylogin"})
	public void roles() throws Exception
	{
	//	pr.openadduserform();
		pr.multiplefilter();
	//	pr.sortroles();
		
	/*	By tablelocator = By.xpath("//table[contains(@class,'MuiTable-root')]");
		String filepath = "C:\\Users\\DELL\\Downloads\\Roles.xlsx";
		pr.getXLSdata(filepath);
		pr.validatetabledata(tablelocator, filepath);
*/	}
	
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