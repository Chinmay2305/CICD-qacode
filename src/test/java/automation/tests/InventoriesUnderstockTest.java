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
import automation.PageObject.PageObject_Inventories_Understock;
import io.opentelemetry.sdk.autoconfigure.spi.ConfigProperties;

public class InventoriesUnderstockTest extends BaseTest
{
	private PageObject_Inventories_Understock pu;

	@BeforeTest
	public void setup() throws IOException, InterruptedException
	{
		super.setup();
		pu = new PageObject_Inventories_Understock(driver);
	}
	
	@Test(priority = 0)
	public void verifylogin() throws Exception
	{
		login();
	}

	@Test(priority = 1, dependsOnMethods = {"verifylogin"})
	public void inventories() throws Exception
	{
		pu.filterinventories();
	//	pu.ChildRowsExpanding_VerifyFiltersforParentAndChild(pu.filterdatasets, pu.attributes);
		pu.ChildRowsExpanding_VerifyTotalQTYforParentAndChild();
	//	pu.sortinventories();
	//	pu.filterinventories();
		pu.sortinventories();
	//	pu.columnconfiguration();
	//	pu.columnpositionchange();
	//	System.out.println("First Hidden - we will check if Historic parts are hidden.");
	//	pu.hidehistoricparts();
	//	System.out.println("Now unhiding - Now we will check if Historic parts are not seen.");
	//	pu.unhidehistoricparts();
			
	/*	Thread.sleep(5000);
		By tablelocator = By.xpath("//table[contains(@class,'MuiTable-root')]");
		String filepath = "C:\\Users\\DELL\\Downloads\\UnderstockInventories.xlsx";
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