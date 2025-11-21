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
import automation.PageObject.PageObject_Login;
import automation.PageObject.PageObject_Inventories;
import io.opentelemetry.sdk.autoconfigure.spi.ConfigProperties;

public class InventoriesTest extends BaseTest
{
	private PageObject_Inventories pi;

	@BeforeTest
	public void setup() throws IOException, InterruptedException
	{
		super.setup();
		pi = new PageObject_Inventories(driver);
	}
	
	@Test(priority = 0)
	public void verifylogin() throws Exception
	{
		login();
	}
	
	@Test(priority = 1, dependsOnMethods = {"verifylogin"})
	public void inventories() throws Exception
	{
			pi.filterinventories();
		//	pi.ChildRowsExpanding_VerifyFiltersforParentAndChild(pi.filterdatasets,pi.attributes); //- Not working correctly
		//	pi.ChildRowsExpanding_VerifyTotalQTYforParentAndChild(); - Not working correctly
		//	pi.rowexpansion_confirmingrowcount();
		//	pi.sortinventories();
		//	pi.filterinventories();
		//	pi.ChildRowsExpanding_VerifyFiltersforParentAndChild(pi.filterdatasets,pi.attributes);
		//	pi.ChildRowsExpanding_VerifyTotalQTYforParentAndChild();
		//	pi.rowexpansion_confirmingrowcount();
		//	pi.sortinventories();
		//	pi.columnconfiguration();
		//	pi.columnpositionchange();
		//	System.out.println("First Hidden - we will check if Historic parts are hidden.");
		//	pi.hidehistoricparts();
		//	System.out.println("Now unhiding - Now we will check if Historic parts are not seen.");
		//	pi.unhidehistoricparts();
		/*	Thread.sleep(5000);
			By tablelocator = By.xpath("//table[contains(@class,'MuiTable-root')]");
			String filepath = "C:\\Users\\DELL\\Downloads\\Inventories.xlsx";
			pi.getXLSdata(filepath);
			pi.validatetabledata(tablelocator, filepath);*/
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